package com.techwells.teammission.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.techwells.teammission.dao.InterfaceMapper;
import com.techwells.teammission.dao.ProjectDynamicMapper;
import com.techwells.teammission.dao.ProjectImageMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.ProjectDynamic;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.BaseServiceUtils;
import com.techwells.teammission.service.ProjectImageService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class ProjectImageServiceImpl implements ProjectImageService {

	@Resource
	private ProjectImageMapper projectImageMapper;

	@Resource
	private InterfaceMapper interfaceMapper;

	@Resource
	private ProjectDynamicMapper dynamicMapper;

	@Resource
	private UserMapper userMapper;

	@Resource
	private RedisUtils redisutils;

	@Value("#{redisParameter.userHashKey}")
	private String USER_KEY; // User中hash的key

	@Value("#{redisParameter.userProjectHashKey}")
	private String UESER_PROJECT;

	@Value("#{redisParameter.projectDynamicListKey}")
	private String PROJECT_DYNAMIC; // 键值key
	
	@Value("#{redisParameter.projectDynamicExpireTime}")
	private Long PROJECT_DYNAMIC_EXPIRE_TIME;   //过期时间

	@Value("#{redisParameter.projectImageHashKey}")
	private String PROJECT_IMAGE;

	@Value("#{redisParameter.projectImageExpireTime}")
	private String PROJECT_IMAGE_EXPIRE_TIME;

	@Value("#{redisParameter.imageInterfaceHashKey}")
	private String IMAGE_INTERFACE; // 键值key
	
	

	/**
	 * 1、上传图片的插入到数据库中 2、数据添加到缓存中 3、发布消息动态
	 */
	@Override
	public Object uploadProjectImageBatch(List<ProjectImage> projectImages,
			Integer userId) throws Exception {
		ResultInfo resultInfo = new ResultInfo();

		if (projectImages.size() == 0) {
			resultInfo.setCode("100002");
			resultInfo.setMessage("请选择图片");
			return resultInfo;
		}
		// 添加数据到数据库中
		int count = projectImageMapper.insertImageBatch(projectImages);

		// 数量不相同，那么上传失败
		if (count < projectImages.size()) {
			throw new RuntimeException(); // 直接抛异常，回滚数据
		}

		// 上传成功，添加缓存中
		try {
			redisutils.addHashObject(PROJECT_IMAGE, projectImages.get(0)
					.getProjectId() + "", projectImages);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// 上传成功之后，发布项目动态

		// 1、根据userId获取用户信息
		// 根据userId获取用户信息 1. 从缓存中获取 2、从mysql中获取

		User user = null;

		try {
			user = (User) redisutils.getHashObject(USER_KEY, userId + "");
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// 如果user==null，缓存中没有数据，那么从mysql中获取
		if (user == null) {
			user = userMapper.selectByPrimaryKey(userId);
			// 如果mysql中也不存在这个用户的信息，那么直接抛出异常回滚数据
			if (user == null) {
				throw new RuntimeException(); //直接抛出异常，回滚数据
			}
		}

		// 此时能够运行到这里，那么user肯定不为null

		ProjectDynamic dynamic = new ProjectDynamic();
		dynamic.setCreatedDate(new Date());
		dynamic.setProjectId(projectImages.get(0).getProjectId());
		dynamic.setUsericon(user.getUserIcon()); // 获取头像
		dynamic.setContent(user.getRealName() + " 上传了原型图 " + " ");

		// 发布项目动态
		try {
			BaseServiceUtils.addDynamic(dynamicMapper, redisutils,
					PROJECT_DYNAMIC, dynamic,PROJECT_DYNAMIC_EXPIRE_TIME);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		resultInfo.setMessage("上传成功");
		return resultInfo;
	}

	@Override
	public Object getImagesByProjectId(Integer projectId) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		// 从缓存中获取数据
		List<ProjectImage> projectImages = null;
		try {
			projectImages = (List<ProjectImage>) redisutils.getHashObject(
					PROJECT_IMAGE, projectId + "");
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// 如果从缓存中获取取到了，那么直接返回即可
		if (projectImages != null) {
			resultInfo.setMessage("获取成功");
			resultInfo.setResult(projectImages);
			resultInfo.setTotal(projectImages.size());
			return resultInfo;
		}

		// 如果从缓存中没有获取到，从mysql中获取
		projectImages = projectImageMapper.selectImagesByProjectId(projectId);

		// mysql中也没有获取到
		if (projectImages.size()==0) {
			resultInfo.setResult(null);
			resultInfo.setTotal(0);
			resultInfo.setMessage("获取成功");
			return resultInfo;
		}

		// mysql中获取到的数据,添加进缓存

		try {
			redisutils.addHashObject(PROJECT_IMAGE, projectId + "",
					projectImages);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		resultInfo.setMessage("获取成功");
		resultInfo.setResult(projectImages);
		resultInfo.setTotal(projectImages.size());
		return resultInfo;
	}

	@Override
	public Object deleteImageByImageId(Integer imageId, Integer projectId)
			throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		List<ProjectImage> projectImages = null;

		// 删除mysql中的数据，删除页面信息，删除接口信息
		int count1 = projectImageMapper.deleteByPrimaryKey(imageId);

		// 删除接口信息
		int count2 = interfaceMapper.deleteFaceByImageId(imageId);

		if (count2 == 0) {
			throw new RuntimeException();
		}

		if (count1 == 0) {
			throw new RuntimeException();
		}

		// 从缓存中获取图片
		try {
			projectImages = (List<ProjectImage>) redisutils.getHashObject(
					PROJECT_IMAGE, projectId + "");
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// 更新缓存中页面的信息
		boolean falg = false; // 标记是否执行的删除的操作
		if (projectImages != null) {
			// 迭代器遍历删除，将不会导致列表删除错误
			Iterator<ProjectImage> iterator = projectImages.iterator();
			while (iterator.hasNext()) {
				ProjectImage projectImage = iterator.next();
				// 如果存在id相等，那么就是需要删除的图片
				if (projectImage.getImageId().equals(imageId)) {
					falg = true;
					iterator.remove(); // 删除即可
				}
			}
		}

		// 如果执行了删除操作，那么需要更新缓存
		if (falg) {
			try {
				// 更新缓存
				redisutils.addHashObject(PROJECT_IMAGE, projectId + "",
						projectImages);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		// 更新缓存中的接口信息
		try {
			// 直接删除缓存中的对应ImageId的所有接口信息
			redisutils.deleteHashObject(IMAGE_INTERFACE, imageId + "");
		} catch (Exception e) {
			throw new Exception();
		}

		resultInfo.setMessage("删除成功");
		return resultInfo;
	}

}
