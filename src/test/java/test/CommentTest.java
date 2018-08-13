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
import com.sun.org.apache.regexp.internal.recompile;
import com.techwells.teammission.dao.CommentMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Comment;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.StringUtil;

public class CommentTest {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		CommentMapper commentMapper=context.getBean("commentMapper",CommentMapper.class);
		
		List<Comment> comments=new ArrayList<Comment>();
		this.Tree(1, commentMapper, comments);
		for (Comment comment : comments) {
			System.out.println(comment);
		}
		
		
	}
	
	
	public void Tree(Integer pid,CommentMapper commentMapper,List<Comment> comments){
		List<Comment> comment=commentMapper.selectCommentByParentId(pid);
		if (comment!=null) {
			comments.addAll(comment);
			//遍历
			for (Comment comment2 : comment) {
				Tree(comment2.getCommentId(), commentMapper, comments);
			}
		}
		
	}
	
	
	
}
