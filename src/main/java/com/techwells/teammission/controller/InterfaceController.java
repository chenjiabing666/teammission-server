package com.techwells.teammission.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.techwells.teammission.domain.Interface;
import com.techwells.teammission.service.InterfaceService;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceController {
	@Resource
	private InterfaceService interfaceService;
	
	
	/**
	 * 添加接口
	 * @param imageId 图片Id
	 * @param faceName   接口名称
	 * @param faceParameters  接口参数
	 * @param entityName  实体类名称
	 * @param requestUrl  请求url
	 * @param publishName  发布人名称
	 * @param request
	 * @return
	 */
	@RequestMapping("/face/addFace")
	public Object addFace(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String imageId=request.getParameter("imageId");
		String faceName=request.getParameter("faceName");
		String faceParameters=request.getParameter("faceParameters");
		String entityName=request.getParameter("entityName");
		String requestUrl=request.getParameter("requestUrl");
		String publishName=request.getParameter("publishName");
		
		//校验参数
		if (StringUtils.isEmpty(imageId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("对应图片Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(faceParameters)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("接口参数不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(entityName)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("实体类名称不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(requestUrl)) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("请求url不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(publishName)) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("发布人姓名不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(imageId)) {
			resultInfo.setCode("100017");
			resultInfo.setMessage("图片Id只能是数字格式");
			return resultInfo;
		}
		
		
		//设置参数
		Interface face=new Interface();
		face.setProjectImageId(Integer.parseInt(imageId));
		face.setCreatedDate(new Date());
		face.setEntityName(entityName);
		face.setInterfaceName(faceName);
		face.setInterfaceUrl(requestUrl);
		face.setParameter(faceParameters);
		face.setUserName(publishName);
		
		//调用service中的方法
		try {
			Object object=interfaceService.addInterface(face);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100018");
			resultInfo.setMessage("添加接口异常");
			return resultInfo;
		}
	}
	
	/**
	 * 根据页面Id获取接口所有接口信息
	 * @param imageId 页面Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/face/getFace")
	public Object getFace(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String imageId=request.getParameter("imageId");
		
		if (StringUtils.isEmpty(imageId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("页面Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(imageId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("页面Id格式不正确");
			return resultInfo;
		}
		
		
		try {
			Object object=interfaceService.getInterface(Integer.parseInt(imageId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("获取接口信息异常");
			return resultInfo;
		}
			
	}
	
	
	
	@RequestMapping("/face/deleteFace")
	public Object deleteFace(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String faceId=request.getParameter("faceId");
		String imageId=request.getParameter("imageId");
		
		//校验参数
		if (StringUtils.isEmpty(faceId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("接口Id不能为空");
			return resultInfo;
		}
		if (StringUtils.isEmpty(imageId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("页面Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(faceId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("接口Id只能是数字");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(imageId)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("页面Id只能是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=interfaceService.deleteInterface(Integer.parseInt(faceId), Integer.parseInt(imageId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("删除异常");
			return resultInfo;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
