package com.itshcool.controller;

import java.io.IOException;
import com.itshcool.annotation.JAction;
import com.itshcool.interceptor.LogInterceptor;
import com.itshcool.model.ClassSeries;
import com.itshcool.param.PageParam;
import com.itshcool.service.SeriesService;
import com.itshcool.util.PageResult;
import com.itshcool.util.QiniuFileUtil;
import com.itshcool.util.Result;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * 课程系列控制器
 * @author chaojunma
 * @date 2018年7月4日
 */
@JAction("/series")
public class ClassSeriesController extends Controller{
	
	private static final SeriesService seriesService = new SeriesService();

	/**
	 * 课程系列分页查询
	 */
	public void list(){
		PageParam param = getBean(PageParam.class, "");
		Page<ClassSeries> data = seriesService.list(param);
		PageResult<ClassSeries> result = new PageResult<>(data);
		renderJson(result);
	}
	
	/**
	 * 保存或更新
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	@Before(LogInterceptor.class)
	public void saveOrUpdate() throws Exception{
		UploadFile file = getFile("cover");
		ClassSeries series = getModel(ClassSeries.class, "");
		if(file != null) {
			String cover = QiniuFileUtil.upload(file);
			series.set("cover", cover);
		}
		if(series.get("id") != null) {
			series.update();
		} else {
			series.save();
		}
		renderJson(new Result());
	}
	
	
	/**
	 * 删除数据
	 */
	@Before(LogInterceptor.class)
	public void delete(){
		seriesService.deleteById(getParaToInt("id"));
		renderJson(new Result());
	}
	
	/**
	 * 批量删除
	 */
	@Before(LogInterceptor.class)
	public void batchDelete(){
		String[] ids = getParaValues("ids[]");
		seriesService.batchDelete(ids);
		renderJson(new Result());
	}
}
