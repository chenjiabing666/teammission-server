package com.techwells.teammission.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.ibatis.javassist.compiler.ast.IntConst;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.sun.istack.internal.logging.Logger;
import com.techwells.teammission.dao.CommentMapper;
import com.techwells.teammission.domain.Comment;
import com.techwells.teammission.domain.rs.CommentUserVos;
import com.techwells.teammission.service.CommentService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class CommentServiceImpl implements CommentService {
	@Resource
	private CommentMapper commentMapper;

	@Resource
	private RedisUtils redisutils;

	@Value("#{redisParameter.commentHashKey}")
	private String COMMENT_KEY; // comment的hashkey

	@Value("#{redisParameter.commentExpireTime}")
	private Long COMMENT_EXPIRE_TIME;
	
	private org.apache.log4j.Logger log=org.apache.log4j.Logger.getLogger(this.getClass());

	@Override
	public Object getComment(Integer weekId) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		List<CommentUserVos> comments = null; // 存放评论

		// 从缓存中获取数据
		try {
			comments = (List<CommentUserVos>) redisutils.getHashObject(
					COMMENT_KEY, "weekId" + weekId);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		if (comments != null) {
			resultInfo.setMessage("成功获取数据");
			resultInfo.setTotal(comments.size());
			resultInfo.setResult(comments);
			return resultInfo;
		}

		// 从mysql中获取数据
		comments = commentMapper.selectComentByWeekId(weekId);
		log.debug("从数据库中获取数据........................");
		if (comments.size()==0) {
			resultInfo.setTotal(0);
		} else {
			resultInfo.setTotal(comments.size());
			// 添加到缓存中,并且设置过期时间
			try {
				redisutils.addHashObject(COMMENT_KEY, "weekId" + weekId,
						comments, COMMENT_EXPIRE_TIME, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		resultInfo.setMessage("成功获取数据");
		resultInfo.setResult(comments);
		return resultInfo;
	}

	/**
	 * 如果只是评论了周报，并不是评论个人的话，那么parentId=null并且toUserId=null
	 */
	@Override
	public Object addComment(Comment comment) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		// 添加到mysql中
		int count = commentMapper.insertSelective(comment);
		// 添加失败
		if (count == 0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("评论发布失败");
			return resultInfo;
		}

		// 发布评论之后，周报下的所有评论的缓存应该更新，因为我们需要获取周报下的所有缓存

		List<CommentUserVos> commentUserVos = null;
		try {
			// 获取周报下的所有评论
			commentUserVos = (List<CommentUserVos>) redisutils.getHashObject(
					COMMENT_KEY, "weekId" + comment.getWeeklyId());

			// 如果缓存中存在数据，那么需要更新
			if (commentUserVos != null) {
				// 根据Id从数据库中获取对应的评论值对象，因为在redis中存储的是值对象，并不是comment对象
				CommentUserVos comment1 = commentMapper
						.selectCommentUserVosByUserIdAndParentId(null, null,
								comment.getCommentId());
				commentUserVos.add(comment1); // 添加在数据中
				// 更新缓存中的数据
				redisutils.addHashObject(COMMENT_KEY,
						"weekId" + comment.getWeeklyId(), commentUserVos,
						COMMENT_EXPIRE_TIME, TimeUnit.SECONDS);
			}

			// 如果缓存中不存在数据，那么不需要更新

		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常
		}

		// 发布评论之后，由于每个评论的详细内容缓存的是该条评论的上面 的全部内容，因此这个新添加的评论对每条评论的对话不需要更新缓存

		// 如果用户名不为空，那么通知这个人
		if (comment.getToUserId() != null) {
			// 暂时不填
		}

		resultInfo.setMessage("添加评论成功");
		return resultInfo;
	}

	/**
	 * 递归获取一个评论中的所有评论
	 * 
	 * @param userId
	 *            评论人的id
	 * @param toUserId
	 *            回复人的id
	 * @param commentId
	 *            评论id
	 * @param comments
	 *            存放所有的评论
	 */
	protected void getComment(Integer userId, Integer toUserId,
			Integer commentId, List<CommentUserVos> comments) {
		CommentUserVos commentUserVos = commentMapper
				.selectCommentUserVosByUserIdAndParentId(userId, toUserId,
						commentId);
		if (commentUserVos != null) {
			comments.add(commentUserVos);
			getComment(commentUserVos.getToUserId(),
					commentUserVos.getUserId(), commentUserVos.getParentId(),
					comments);
		}
	}

	@Override
	public Object getSomeOneComment(Integer userId, Integer toUserId,
			Integer commentId) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		// 从缓存中获取

		List<CommentUserVos> commentUserVos = null;
		try {
			commentUserVos = (List<CommentUserVos>) redisutils.getHashObject(
					COMMENT_KEY, commentId + "");
		} catch (Exception e) {
			throw new RuntimeException();
		}

		if (commentUserVos != null) {
			resultInfo.setMessage("获取成功");
			resultInfo.setTotal(commentUserVos.size());
			resultInfo.setResult(commentUserVos);
			return resultInfo;
		}

		// 如果为空，从mysql中获取
		commentUserVos = new ArrayList<CommentUserVos>();

		this.getComment(userId, toUserId, commentId, commentUserVos);

		// 如果数据库中存在数据，那么放入缓存中即可,放入缓存之前一定需要逆置
		if (commentUserVos.size() > 0) {
			try {
				// 逆置集合
				Collections.reverse(commentUserVos);
				redisutils.addHashObject(COMMENT_KEY, commentId + "",
						commentUserVos);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		resultInfo.setMessage("获取成功");
		resultInfo.setResult(commentUserVos);
		resultInfo.setTotal(commentUserVos.size());
		return resultInfo;

	}

	/**
	 * 删除一个顶级评论，直接对周报发布评论，不是回复的评论，那么可以直接删除，更新该周报下的所有缓存即可，不用更新该条评论的详细内容的缓存
	 * 删除一个不是顶级评论对缓存的影响 1、需要更新整个周报中评论信息的缓存，将缓存中的此评论信息删除
	 * 2、每条评论详细信息的缓存是以userId_toUserId_commentId缓存的 1、肯定是需要删除该条评论的所有缓存的详细信息
	 * 2、对所有回复的详细内容中，对该条评论之前的缓存是没有影响的，因此不需要删除之前的缓存
	 * 3、对所有回复的详细内容中，对该条评论后面的缓存肯定是有影响的
	 * ，因为在缓存中会有该条评论的内容，我们应该将其删除，此时我们就需要递归查询该条评论后面的所有评论信息，并且将其中的缓存信息删除
	 */
	@Override
	public Object deleteComment(Integer commentId, Integer weekId)
			throws Exception {
		ResultInfo resultInfo = new ResultInfo();

		// 先根据commentId获取该条评论的详细信息
		Comment comment1 = commentMapper.selectByPrimaryKey(commentId);

		// 先删除数据库中的该条信息，直接删除即可，不必考虑整体的影响
		int count = commentMapper.deleteByPrimaryKey(commentId);
		// 删除失败
		if (count == 0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("删除失败");
			return resultInfo;
		}

		// 如果不是顶级评论，那么查找所有的和这个节点有管关联的节点信息
		if (!comment1.getParentId().equals(0)) {
			List<Comment> comments2 = new ArrayList<Comment>(); // 存储所有的受影响的评论

			// 获取一级子节点，这些节点肯定是受影响的
			comments2 = commentMapper.selectCommentByParentId(
					comment1.getCommentId(), null, null);

			// 获取该消息对应的人的回复信息
			getCommentByParentId(commentId, comment1.getToUserId(),
					comment1.getUserId(), comments2);

			// 更新缓存中的内容，不直接更新了，直接删除field即可
			for (Comment comment : comments2) {
				try {
					redisutils.deleteHashObject(COMMENT_KEY,
							comment.getCommentId() + "");
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}

		}

		// 删除该条评论的缓存信息
		
		try {
			redisutils.deleteHashObject(COMMENT_KEY, commentId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		

		/**
		 * 删除周报下的整体缓存 1、获取周报下的所有缓存信息 2、遍历删除 3、写入缓存
		 */
		List<CommentUserVos> commentUserVos = null;
		try {
			commentUserVos = (List<CommentUserVos>) redisutils.getHashObject(
					COMMENT_KEY, weekId + "");
			// 如果缓存中存在这个信息，那么需要更新，不存在直接pass
			if (commentUserVos != null) {
				boolean flag = false; // 标记是否执行了删除
				// 迭代器遍历删除
				Iterator<CommentUserVos> iterator = commentUserVos.iterator();
				while (iterator.hasNext()) {
					CommentUserVos comment = iterator.next();
					// 如果存在这个数据，那么直接删除即可
					if (comment.getCommentId().equals(commentId)) {
						iterator.remove(); // 删除即可
						flag = true;
					}
				}

				// 如果执行了删除，那么需要更新缓存
				if (flag) {
					try {
						// 更新缓存
						redisutils.addHashObject(COMMENT_KEY, weekId + "",
								commentUserVos);
					} catch (Exception e) {
						throw new RuntimeException();
					}
				}

			}
			// 如果缓存中不存在数据，那么不需要更新缓存，直接pass
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("删除成功");
		return resultInfo;
	}

	//递归的根据父节点获取详细的对话内容
	protected void getCommentByParentId(Integer parentId, Integer userId,
			Integer toUserId, List<Comment> comments) {
		List<Comment> cm = commentMapper.selectCommentByParentId(parentId,
				userId, toUserId);
		if (cm != null) {
			comments.addAll(cm); // 添加到集合中
			// 遍历获取下面的所有子评论
			for (Comment comment : cm) {
				getCommentByParentId(comment.getCommentId(),
						comment.getToUserId(), comment.getUserId(), comments);
			}
		}
	}

}
