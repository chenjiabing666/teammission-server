package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.techwells.teammission.dao.InterfaceMapper;
import com.techwells.teammission.dao.ProjectDynamicMapper;
import com.techwells.teammission.dao.ProjectImageMapper;
import com.techwells.teammission.dao.ProjectMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Interface;
import com.techwells.teammission.domain.ProjectDynamic;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.InterfaceService;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.StringUtil;

public class TestProject {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		ProjectDynamicMapper dynamicMapper=context.getBean("projectDynamicMapper",ProjectDynamicMapper.class);
		ProjectDynamic dynamic=new ProjectDynamic();
		dynamic.setContent("cdcds");
		dynamic.setUsericon("cds");
		dynamic.setCreatedDate(new Date());
		dynamic.setProjectId(1);
		dynamic.setDeleted(1);
		dynamicMapper.insert(dynamic);
		context.close();
	}
	
	
	@Test
	public void test2(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		ProjectImageMapper projectImageMapper=context.getBean("projectImageMapper",ProjectImageMapper.class);
		List<ProjectImage> projectImages=new ArrayList<ProjectImage>();
		
		projectImages=projectImageMapper.selectImagesByProjectId(1);
		System.out.println(projectImages);
		
		context.close();
	}
	
	
	
	
}
