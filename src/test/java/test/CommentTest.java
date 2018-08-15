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

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.recompile;
import com.techwells.teammission.dao.CommentMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Comment;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.domain.rs.CommentUserVos;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.StringUtil;

public class CommentTest {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		CommentMapper commentMapper=context.getBean("commentMapper",CommentMapper.class);
//		CommentUserVos commentUserVos=commentMapper.selectCommentUserVosByUserIdAndParentId(3, 2, 3);
//		System.out.println(commentUserVos);
	Comment comment=null;
	comment=commentMapper.selectByPrimaryKey(11);
	System.out.println(comment);
								

		
	}
	
	
	public void Tree(Integer userId,Integer toUserId,Integer commentId,CommentMapper commentMapper,List<CommentUserVos> comments){
		CommentUserVos commentUserVos=commentMapper.selectCommentUserVosByUserIdAndParentId(userId, toUserId, commentId);
		if (commentUserVos!=null) {
			comments.add(commentUserVos);
			Tree(commentUserVos.getToUserId(), commentUserVos.getUserId(),commentUserVos.getParentId(), commentMapper, comments);
		}
	}
	
	public void getComment(Integer parentId,Integer userId,Integer toUserId,List<Comment> comments,CommentMapper commentMapper){
		List<Comment> cm=commentMapper.selectCommentByParentId(parentId,userId,toUserId);
		if (cm!=null) {
			comments.addAll(cm);  //添加到集合中
			//遍历获取下面的所有子评论
			for (Comment comment : cm) {
				getComment(comment.getCommentId(), comment.getToUserId(), comment.getUserId(), comments, commentMapper);
			}
		}
	}
	
	
	
}
