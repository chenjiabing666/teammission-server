package com.techwells.teammission.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import com.techwells.teammission.dao.InterfaceMapper;
import com.techwells.teammission.dao.ProjectImageMapper;
import com.techwells.teammission.domain.Interface;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.service.InterfaceService;
import com.techwells.teammission.service.ProjectImageService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class InterfaceServiceImpl implements InterfaceService {
	
	@Resource
	private InterfaceMapper interfaceMapper;
	
	@Resource
	private RedisUtils redisutils;
	
	@Value("#{redisParameter.imageInterfaceHashKey}")
	private String IMAGE_INTERFACE;      //键值key
	
	@Value("#{redisParameter.imageInterfaceExpireTime}")
	private String IMAGE_INTERFACE_EXPIRE_TIME;   //过期时间

	@Override
	public Object addInterface(Interface face)
			throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		//在mysql中添加
		int count=interfaceMapper.insertSelective(face);
		
		//添加失败
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("添加失败");
			return resultInfo;
		}
		
		//添加成功之后，在redis中添加
		
		//从redis获取指定Image_id中的List<Interface>，在其中添加一个
		
		List<Interface> faces=null;
		try {
			faces=(List<Interface>) redisutils.getHashObject(IMAGE_INTERFACE, face.getProjectImageId()+"");
			//缓存中没有数据，那么直接添加
			if (faces==null) {
				faces=new ArrayList<Interface>();
				faces.add(face);
			}else {   //如果！=null，表明已经存在接口
				faces.add(face);
			}
			
			//添加缓存
			redisutils.addHashObject(IMAGE_INTERFACE, face.getProjectImageId()+"", faces);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		resultInfo.setMessage("添加成功");
		return resultInfo;
	}

	
	@Override
	public Object getInterface(Integer imageId)throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		
		List<Interface> faces=null;
		//从缓存中获取数据
		try {
			faces=(List<Interface>) redisutils.getHashObject(IMAGE_INTERFACE, imageId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//如果从缓存中能够获取到数据，那么直接返回即可
		if (faces!=null) {
			resultInfo.setResult(faces);
			resultInfo.setTotal(faces.size());
			resultInfo.setMessage("获取成功");
			return resultInfo;
		}
		
		//如果没有获取到，只能从mysql中获取
		//从mysql中获取数据
		faces=interfaceMapper.selectInterFaceByImageId(imageId);
		
		//没有接口信息，直接返回信息，不过不需要缓存了
		if (faces==null) {
			resultInfo.setResult(null);
			resultInfo.setTotal(0);
			resultInfo.setMessage("获取成功");
			return resultInfo;
		}
		
		//face！=null，缓存
		try {
			redisutils.addHashObject(IMAGE_INTERFACE, imageId+"", faces);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setResult(faces);
		resultInfo.setTotal(faces.size());
		resultInfo.setMessage("获取成功");
		return resultInfo;
	}


	
	
	@Override
	public Object deleteInterface(Integer faceId,Integer imageId) throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		
		//删除mysql中的信息
		int count=interfaceMapper.deleteByPrimaryKey(faceId);
		
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("删除失败");
			return resultInfo;
		}
		
		//如果删除成功，删除缓存中的数据
		
		List<Interface> faces=null;
		
		try {
			//从缓存中根据imageId获取所有的接口信息
			faces=(List<Interface>) redisutils.getHashObject(IMAGE_INTERFACE,imageId+"");
		} catch (Exception e) {
			throw new Exception();
		}
		
		boolean flag=false;  //标记是否删除
		//如果缓存中存在数据，那么就需要比较删除，如果没有数据，那么不需要删除
		if (faces!=null) {
			Iterator<Interface> iterator=faces.iterator();
			while(iterator.hasNext()){
				Interface face=iterator.next();
				//如果id相同，那么就是需要删除的数据
				if (face.getInterfaceId().equals(faceId)) {
					iterator.remove();
					flag=true;
				}
			}
			
			//如果删除了数据，那么需要更新缓存,如果没有删除数据，那么不需要更新数据
			if (flag) {
				try {
					redisutils.addHashObject(IMAGE_INTERFACE, imageId+"", faces);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
		resultInfo.setMessage("删除成功");
		return resultInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
