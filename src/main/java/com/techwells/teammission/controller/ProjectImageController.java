package com.techwells.teammission.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.coyote.RequestGroupInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.service.ProjectImageService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;
import com.techwells.teammission.util.TeammissionConstants;
import com.techwells.teammission.util.UploadImageUtil;

@RestController
public class ProjectImageController {
	@Resource
	private ProjectImageService projectImageService;
	
	
	/**
	 * 上传文件，形成缩略图片
	 * 
	 * @param request
	 * @param files  文件
	 * @param projectId  项目Id
	 * @return
	 */
	@RequestMapping("/image/upload")
	public Object upload(HttpServletRequest request,@RequestParam("files")MultipartFile[] files,Integer projectId,Integer userId){
		ResultInfo resultInfo=new ResultInfo();
		
		if (files==null||files.length==0) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("请选择上传的图片");
			return resultInfo;
		}
		
		
		if (projectId==null) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		
		List<ProjectImage> projectImages=new ArrayList<ProjectImage>();
		//遍历数组
		for (MultipartFile file : files) {
			// /usr/share/nginx/html/www/teammission-upload/projectImage/projectId/
			String filePath=TeammissionConstants.UPLOAD_PATH+"/projectImage/"+projectId+"/";  //上传的文件路径
			String fileName=file.getOriginalFilename();  //获取文件的名称
			
			File targetFile=new File(filePath,fileName);
			
			//如果文件夹不存在，那么创建
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			
			String showImage;  //缩略图url
			try {
				//生成缩略图
				showImage=UploadImageUtil.thumbnailUploadImage(file, 100, 100, TeammissionConstants.UPLOAD_URL, filePath);
				file.transferTo(targetFile);   //上传文件
			} catch (Exception e) {
				resultInfo.setCode("100013");
				resultInfo.setMessage("上传失败");
				return resultInfo;
			}
			
			//图片的url
			String imageUrl=TeammissionConstants.UPLOAD_URL+"/projectImage/"+projectId+"/"+fileName;
			
			ProjectImage projectImage=new ProjectImage();
			projectImage.setProjectId(projectId);
			projectImage.setCreatedDate(new Date());
			projectImage.setImageUrl(imageUrl);
			projectImage.setShowImage(showImage);
			//添加到List中
			projectImages.add(projectImage);
		}
		
		try {
			Object object=projectImageService.uploadProjectImageBatch(projectImages,userId);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("上传异常");
			return resultInfo;
		}
	}
	
	
	/**
	 * 根据项目id获取图片
	 * @param projectId 项目id
	 * @param request
	 * @return
	 */
	@RequestMapping("/image/getImagsByProjectId")
	public Object getImagesByProjectId(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String projectId=request.getParameter("projectId");
		
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(projectId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("项目Id只能是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=projectImageService.getImagesByProjectId(Integer.parseInt(projectId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("获取图片异常");
			return resultInfo;
		}
		
	}
	
	
	@RequestMapping("/image/deleteImage")
	public Object deleteImage(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String imageId=request.getParameter("imageId");
		String projectId=request.getParameter("projectId");
		
		//校验参数
		if (StringUtils.isEmpty(imageId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("图片Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(projectId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("项目Id必须是数字");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(imageId)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("图片Id必须是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=projectImageService.deleteImageByImageId(Integer.parseInt(imageId), Integer.parseInt(projectId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("删除异常");
			return resultInfo;
		}
		
	}
	
	
	
	
}
