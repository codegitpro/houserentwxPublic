package com.cl.controller;

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

import com.cl.utils.ValidatorUtils;
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
import com.cl.annotation.IgnoreAuth;

import com.cl.entity.YuyuekanfangEntity;
import com.cl.entity.view.YuyuekanfangView;

import com.cl.service.YuyuekanfangService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.CommonUtil;
import java.io.IOException;

/**
 * 预约看房
 * 后端接口
 * @author 
 * @email 
 * @date 2024-05-15 17:48:57
 */
@RestController
@RequestMapping("/yuyuekanfang")
public class YuyuekanfangController {
    @Autowired
    private YuyuekanfangService yuyuekanfangService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuyuekanfangEntity yuyuekanfang,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("fangdong")) {
			yuyuekanfang.setFangdongzhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("yonghu")) {
			yuyuekanfang.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<YuyuekanfangEntity> ew = new EntityWrapper<YuyuekanfangEntity>();

		PageUtils page = yuyuekanfangService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyuekanfang), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YuyuekanfangEntity yuyuekanfang, 
		HttpServletRequest request){
        EntityWrapper<YuyuekanfangEntity> ew = new EntityWrapper<YuyuekanfangEntity>();

		PageUtils page = yuyuekanfangService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyuekanfang), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YuyuekanfangEntity yuyuekanfang){
       	EntityWrapper<YuyuekanfangEntity> ew = new EntityWrapper<YuyuekanfangEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuyuekanfang, "yuyuekanfang")); 
        return R.ok().put("data", yuyuekanfangService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuyuekanfangEntity yuyuekanfang){
        EntityWrapper< YuyuekanfangEntity> ew = new EntityWrapper< YuyuekanfangEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuyuekanfang, "yuyuekanfang")); 
		YuyuekanfangView yuyuekanfangView =  yuyuekanfangService.selectView(ew);
		return R.ok("查询预约看房成功").put("data", yuyuekanfangView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuyuekanfangEntity yuyuekanfang = yuyuekanfangService.selectById(id);
		yuyuekanfang = yuyuekanfangService.selectView(new EntityWrapper<YuyuekanfangEntity>().eq("id", id));
        return R.ok().put("data", yuyuekanfang);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuyuekanfangEntity yuyuekanfang = yuyuekanfangService.selectById(id);
		yuyuekanfang = yuyuekanfangService.selectView(new EntityWrapper<YuyuekanfangEntity>().eq("id", id));
        return R.ok().put("data", yuyuekanfang);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody YuyuekanfangEntity yuyuekanfang, HttpServletRequest request){
    	yuyuekanfang.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(yuyuekanfang);
        yuyuekanfangService.insert(yuyuekanfang);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody YuyuekanfangEntity yuyuekanfang, HttpServletRequest request){
    	yuyuekanfang.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(yuyuekanfang);
        yuyuekanfangService.insert(yuyuekanfang);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody YuyuekanfangEntity yuyuekanfang, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuyuekanfang);
        yuyuekanfangService.updateById(yuyuekanfang);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<YuyuekanfangEntity> list = new ArrayList<YuyuekanfangEntity>();
        for(Long id : ids) {
            YuyuekanfangEntity yuyuekanfang = yuyuekanfangService.selectById(id);
            yuyuekanfang.setSfsh(sfsh);
            yuyuekanfang.setShhf(shhf);
            list.add(yuyuekanfang);
        }
        yuyuekanfangService.updateBatchById(list);
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        yuyuekanfangService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








}
