package com.techwells.teammission.domain.rs;

import java.io.Serializable;
import java.util.Date;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * 用户和评论的值对象
 * @author Administrator
 *
 */
public class CommentUserVos implements Serializable {
	private static final long serialVersionUID = -6789450271108407956L;
	private Integer commentId; 
	private Integer parentId;
	private Integer userId;
	private String userIcon;
	private String userName;
	private String realName;
	private Date replayTime;  //回复的时间
	private String content;  //评论的内容
	private String toUserName;
	
}
