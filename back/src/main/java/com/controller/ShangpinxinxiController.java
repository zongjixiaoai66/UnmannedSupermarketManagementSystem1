package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.entity.OrdersEntity;
import com.service.OrdersService;

import com.entity.ShangpinxinxiEntity;
import com.entity.view.ShangpinxinxiView;

import com.service.ShangpinxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 商品信息
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-30 20:54:05
 */
@RestController
@RequestMapping("/shangpinxinxi")
public class ShangpinxinxiController {
    @Autowired
    private ShangpinxinxiService shangpinxinxiService;

    @Autowired
    private StoreupService storeupService;

    @Autowired
    private OrdersService ordersService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi,
                @RequestParam(required = false) Double pricestart,
                @RequestParam(required = false) Double priceend,
		HttpServletRequest request){
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
                if(pricestart!=null) ew.ge("price", pricestart);
                if(priceend!=null) ew.le("price", priceend);

		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi, 
                @RequestParam(required = false) Double pricestart,
                @RequestParam(required = false) Double priceend,
		HttpServletRequest request){
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
                if(pricestart!=null) ew.ge("price", pricestart);
                if(priceend!=null) ew.le("price", priceend);

		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShangpinxinxiEntity shangpinxinxi){
       	EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shangpinxinxi, "shangpinxinxi")); 
        return R.ok().put("data", shangpinxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShangpinxinxiEntity shangpinxinxi){
        EntityWrapper< ShangpinxinxiEntity> ew = new EntityWrapper< ShangpinxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shangpinxinxi, "shangpinxinxi")); 
		ShangpinxinxiView shangpinxinxiView =  shangpinxinxiService.selectView(ew);
		return R.ok("查询商品信息成功").put("data", shangpinxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
		shangpinxinxi.setClicktime(new Date());
		shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok().put("data", shangpinxinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
		shangpinxinxi.setClicktime(new Date());
		shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok().put("data", shangpinxinxi);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
        if(type.equals("1")) {
        	shangpinxinxi.setThumbsupnum(shangpinxinxi.getThumbsupnum()+1);
        } else {
        	shangpinxinxi.setCrazilynum(shangpinxinxi.getCrazilynum()+1);
        }
        shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok("投票成功");
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
    	shangpinxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.insert(shangpinxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
    	shangpinxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.insert(shangpinxinxi);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.updateById(shangpinxinxi);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        shangpinxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request,String pre){
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));
        return R.ok().put("data", page);
    }


        /**
     * 协同算法（按用户购买推荐）
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
        String userId = request.getSession().getAttribute("userId").toString();
        String goodtypeColumn = "shangpinfenlei";
        List<OrdersEntity> orders = ordersService.selectList(new EntityWrapper<OrdersEntity>().eq("userid", userId).eq("tablename", "shangpinxinxi").orderBy("addtime", false));
        List<String> goodtypes = new ArrayList<String>();
        Integer limit = params.get("limit")==null?10:Integer.parseInt(params.get("limit").toString());
        List<ShangpinxinxiEntity> shangpinxinxiList = new ArrayList<ShangpinxinxiEntity>();
	//去重
    	List<OrdersEntity> ordersDist = new ArrayList<OrdersEntity>();
    	for(OrdersEntity o1 : orders) {
    		boolean addFlag = true;
    		for(OrdersEntity o2 : ordersDist) {
    			if(o1.getGoodid()==o2.getGoodid() || o1.getGoodtype().equals(o2.getGoodtype())) {
    				addFlag = false;
    				break;
    			}
    		}
    		if(addFlag) ordersDist.add(o1);
    	}
        if(ordersDist!=null && ordersDist.size()>0) {
                for(OrdersEntity o : ordersDist) {
                        shangpinxinxiList.addAll(shangpinxinxiService.selectList(new EntityWrapper<ShangpinxinxiEntity>().eq(goodtypeColumn, o.getGoodtype())));
                }
        }
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
        params.put("sort", "id");
        params.put("order", "desc");
        PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));
        List<ShangpinxinxiEntity> pageList = (List<ShangpinxinxiEntity>)page.getList();
        if(shangpinxinxiList.size()<limit) {
                int toAddNum = (limit-shangpinxinxiList.size())<=pageList.size()?(limit-shangpinxinxiList.size()):pageList.size();
                for(ShangpinxinxiEntity o1 : pageList) {
                    boolean addFlag = true;
                    for(ShangpinxinxiEntity o2 : shangpinxinxiList) {
                        if(o1.getId().intValue()==o2.getId().intValue()) {
                            addFlag = false;
                            break;
                        }
                    }
                    if(addFlag) {
                        shangpinxinxiList.add(o1);
                        if(--toAddNum==0) break;
                    }   
                }
        } else if(shangpinxinxiList.size()>limit) {
            shangpinxinxiList = shangpinxinxiList.subList(0, limit);
        }
        page.setList(shangpinxinxiList);
        return R.ok().put("data", page);
    }







}
