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
import com.techwells.teammission.dao.ProjectImageMapper;
import com.techwells.teammission.dao.ProjectMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.StringUtil;

public class TestProject {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		ProjectImageMapper projectImageMapper=context.getBean("projectImageMapper",ProjectImageMapper.class);
		List<ProjectImage> projectImages=new ArrayList<ProjectImage>();
		
		ProjectImage projectImage=new ProjectImage();
		projectImage.setImageName("cd");
		
		ProjectImage projectImage1=new ProjectImage();
		projectImage1.setImageName("cd");
		projectImages.add(projectImage);
		projectImages.add(projectImage1);
		projectImageMapper.insertImageBatch(projectImages);
		
		System.out.println(projectImage.getImageId());
		context.close();
	}
	
	
	
	
	
}
