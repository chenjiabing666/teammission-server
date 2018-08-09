package com.techwells.teammission.controller;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import sun.management.resources.agent;

import com.sun.crypto.provider.RSACipher;
import com.techwells.teammission.domain.CRCode;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.DateUtil;
import com.techwells.teammission.util.PagingTool;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.SendSmsUtil;
import com.techwells.teammission.util.StringUtil;
import com.techwells.teammission.util.TeammissionConstants;

@RestController
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class); // log4j

	@Resource
	private UserService userService;

	/**
	 * 根据id获取用户信息
	 * 
	 * @param userId
	 *            用户Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/getUserByUserId")
	public Object getUserByUserId(HttpServletRequest request) {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集

		// 判断参数
		String userId = request.getParameter("userId");
		if (StringUtils.isEmpty(userId)) {
			rsInfo.setCode("100011");
			rsInfo.setMessage("用户id不能为空");
			return rsInfo;
		}

		if (!StringUtil.isNumber(userId)) {
			rsInfo.setCode("100013");
			rsInfo.setMessage("用户Id必须是数字");
			return rsInfo;
		}

		try {
			Object object = userService.getUserByUserId(Integer
					.parseInt(userId));
			return object;
		} catch (Exception e) {
			rsInfo.setCode("100012");
			rsInfo.setMessage("获取用户数据异常");
			return rsInfo;
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/addUser")
	public Object addUser(HttpServletRequest request) {
		ResultInfo reinfo = new ResultInfo();

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		// 检查参数的情况
		if (StringUtils.isEmpty(userName)) {
			reinfo.setCode("100011");
			reinfo.setMessage("用户姓名不能为空");
			return reinfo;
		}

		if (StringUtils.isEmpty(password)) {
			reinfo.setCode("100012");
			reinfo.setMessage("用户年龄不能为空");
			return reinfo;
		}

		User user = new User();

		try {
			Object object = userService.addUser(user);
			return object;
		} catch (Exception e) {
			reinfo.setCode("100013");
			reinfo.setMessage("添加用户异常");
			return reinfo;
		}
	}

	/**
	 * <p>
	 * 修改用户信息
	 * </p>
	 * 
	 * @param userId
	 *            必填
	 * @param userIcon
	 *            用户头像 可选
	 * @param name
	 *            姓名 可选
	 * @param job
	 *            职位 可选
	 * @param mobile
	 *            手机号码 可选
	 * @param address
	 *            地址 可选
	 * @param email  邮箱
	 * @return
	 */
	@RequestMapping("/user/modifyUser")
	public Object modifyUser(HttpServletRequest request) {
		ResultInfo reinfo = new ResultInfo();

		String userId = request.getParameter("userId");
		String realName = request.getParameter("name");
		String job = request.getParameter("job"); // 职位
		String mobile = request.getParameter("mobile");
		String address = request.getParameter("address");
		String email=request.getParameter("email");

		// 参数校验
		if (StringUtils.isEmpty(userId)) {
			reinfo.setCode("100011");
			reinfo.setMessage("用户id不能为空");
			return reinfo;
		}

		if (!StringUtil.isNumber(userId)) {
			reinfo.setCode("100012");
			reinfo.setMessage("用户id必须是数字");
			return reinfo;
		}

		if (StringUtils.isEmpty(realName)) {
			reinfo.setCode("100013");
			reinfo.setMessage("姓名不能为空");
			return reinfo;
		}

		if (StringUtils.isEmpty(job)) {
			reinfo.setCode("100014");
			reinfo.setMessage("职位不能为空");
			return reinfo;
		}

		if (StringUtils.isEmpty(mobile)) {
			reinfo.setCode("100015");
			reinfo.setMessage("手机号码不能为空");
			return reinfo;
		}
		
		
		if (StringUtils.isEmpty(email)) {
			reinfo.setCode("100016");
			reinfo.setMessage("邮箱不能为空");
			return reinfo;
		}
		
		
		//校验邮箱格式
		if (!StringUtil.isEmail(email)) {
			reinfo.setCode("100017");
			reinfo.setMessage("请输入正确的邮箱格式");
			return reinfo;
		}
		
		// 校验手机号码格式
		if (StringUtil.isMobile(mobile)) {
			reinfo.setCode("100017");
			reinfo.setMessage("请输入正确的手机号码格式");
			return reinfo;
		}

		if (StringUtils.isEmpty(address)) {
			reinfo.setCode("100016");
			reinfo.setMessage("地址不能为空");
			return reinfo;
		}

		User user = new User(); // 创建User对象
		user.setUserId(Integer.parseInt(userId)); // 设置id
		user.setUpdatedDate(new Date()); // 设置修改日期
		user.setRealName(realName);
		user.setJob(job);
		user.setMobile(mobile);
		user.setAddress(address);
		user.setEmail(email);

		// 处理头像
		String contentType = request.getContentType(); // 获取请求类型
		if (contentType != null
				&& contentType.toLowerCase().startsWith("multipart/")) {
			MultipartHttpServletRequest multipartRequest = WebUtils
					.getNativeRequest(request,
							MultipartHttpServletRequest.class);
			MultipartFile file = multipartRequest.getFile("userIcon"); // 获取用户头像

			if (file != null) {
				String path = TeammissionConstants.UPLOAD_PATH + "user-image/";
				String fileName = file.getOriginalFilename(); // 文件的真实名字
				String filePath = System.currentTimeMillis() + fileName; // 此时的文件名，用来区分
				File targetFile = new File(path, filePath);

				// 如果这个文件夹不存在，那么久创建一个
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}

				try {
					file.transferTo(targetFile); // 上传图片
				} catch (Exception e) {
					e.printStackTrace();
					reinfo.setMessage("上传图片出错");
					reinfo.setCode("10001");
					return reinfo;
				}
				String urlPath = TeammissionConstants.UPLOAD_URL
						+ "user-image/" + filePath;
				user.setUserIcon(urlPath); // 设置头像的url
			}
		}

		try {
			Object object = userService.modifyUser(user); // 调用修改的方法
			return object;
		} catch (Exception e) {
			reinfo.setCode("100016");
			reinfo.setMessage("修改用户异常");
			return reinfo;
		}
	}

	/**
	 * 修改用户的账号和密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/modifyPasswor")
	public Object modifyPassword(HttpServletRequest request){
		ResultInfo reinfo=new ResultInfo();
		//获取参数
		String userId=request.getParameter("userId");
		String oldPwd=request.getParameter("oldPwd");
		String newPwd=request.getParameter("newPwd");
		
		
		if (StringUtils.isEmpty(userId)) {
			reinfo.setCode("100010");
			reinfo.setMessage("用户Id不能为空");
			return reinfo;
		}
		
		
		if (StringUtils.isEmpty(oldPwd)) {
			reinfo.setCode("100013");
			reinfo.setMessage("旧密码不能为空");
			return reinfo;
		}
		
		if (StringUtils.isEmpty(newPwd)) {
			reinfo.setCode("100014");
			reinfo.setMessage("新密码不能为空");
			return reinfo;
		}
		
		if (!StringUtil.isNumber(userId)) {
			reinfo.setCode("100017");
			reinfo.setMessage("用户Id只能是数字");
			return reinfo;
		}
		
		User user=null;
		
		try {
			Object object=userService.modifyPassword(Integer.parseInt(userId),oldPwd, newPwd);
			return object;
		} catch (Exception e) {
			reinfo.setCode("100018");
			reinfo.setMessage("修改异常");
			return reinfo;
		}
		
	}
	
	
	
	
	/**
	 * 一个小时之内只能发送10条短信
	 * @param mobile  手机号码
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/user/sendMessage")
	public Object sendMessage(HttpServletRequest request,HttpSession session){
		ResultInfo resultInfo=new ResultInfo();
		String mobile=request.getParameter("mobile");
		
		//校验参数
		if (StringUtils.isEmpty("mobile")) {
			resultInfo.setCode("100010");
			resultInfo.setMessage("手机号码不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isMobile(mobile)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("手机号码格式不正确，请重新输入");
			return resultInfo;
		}
		
		
		Object object=session.getAttribute("bindCode");  //获取session中的信息
		
		String randomeCode=SendSmsUtil.getFourRadam();  //获取随机的验证码
		
		
		//表示还没有发送过短信,那么可以直接发送短信
		if (object==null) {
			try {
				SendSmsUtil.sendUserCrCode(mobile, randomeCode);
				CRCode crCode=new CRCode();   //封装验证码信息
				crCode.setMobile(mobile);
				crCode.setCrCode(randomeCode);
				crCode.setNumber(1);   //数量设置一条
				crCode.setSendDate(new Date());  //发送时间
				session.setAttribute("", crCode);   //添加到session中
			} catch (Exception e) {
				resultInfo.setCode("100012");
				resultInfo.setMessage("获取验证码异常");
				return resultInfo;
			}
			
		}else {  //如果不为空，表示之前发送过验证码
			CRCode crCode=(CRCode)object;  //强转
			//判断发送时间
			Long sendMils=crCode.getSendDate().getTime();   //获取发送短信的毫秒
			
			//如果发送的时间大于一小时，那么没有限制，可以直接发送
			if ((System.currentTimeMillis()-sendMils)/1000/3600>1) {
				try {
					SendSmsUtil.sendUserCrCode(mobile, randomeCode);
					CRCode crCode1=new CRCode();   //封装验证码信息
					crCode1.setMobile(mobile);
					crCode1.setCrCode(randomeCode);
					crCode1.setNumber(1);   //数量设置一条
					crCode1.setSendDate(new Date());  //发送时间
					session.setAttribute("", crCode1);   //添加到session中
				} catch (Exception e) {
					resultInfo.setCode("100013");
					resultInfo.setMessage("获取验证码异常");
					return resultInfo;
				}
			}else {   //如果小于一小时，需要判断发送的条数
				int num=crCode.getNumber();  //获取发送的条数
				
				if (num<10) {   //如果发送的短信小于10条，可以直接发送
					try {
						SendSmsUtil.sendUserCrCode(mobile, randomeCode);
						crCode.setNumber(crCode.getNumber()+1);  //条数+1
						crCode.setSendDate(new Date());  //设置发送时间
						crCode.setMobile(mobile);  //设置发送的手机号码
						crCode.setCrCode(randomeCode);  //设置验证码
						session.setAttribute("", crCode);   //添加到session中
					} catch (Exception e) {
						resultInfo.setCode("100014");
						resultInfo.setMessage("获取验证码异常");
						return resultInfo;
					}
				}else {   //此时已经超过10条了，那么此时不能在发送了
					resultInfo.setCode("100015");
					resultInfo.setMessage("1小时之内已经发送超过了10条");
					return resultInfo;
				}
				
			}
		}
		
		resultInfo.setMessage("验证码发送成功");
		return resultInfo;		
	}
	
	
	
	/**
	 * 绑定手机号码
	 * @param mobile  手机号码
	 * @param userId  用户Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/bindMobile")
	public Object bindMobile(HttpServletRequest request,HttpSession session){
		ResultInfo resultInfo=new ResultInfo();
		String mobile=request.getParameter("mobile");
		String userId=request.getParameter("userId");
		String code=request.getParameter("code");
		
		//校验参数
		if (StringUtils.isEmpty(mobile)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("手机号码不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
		
		if (StringUtil.isMobile(mobile)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("手机号码格式不正确，请重新输入");
			return resultInfo;
		}
		
		if (StringUtil.isNumber(userId)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("用户Id只能是数字");
			return resultInfo;
		}
		
		CRCode crcode=(CRCode) session.getAttribute("bindCode");
		
		if (crcode==null) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("请点击发送验证码");
			return resultInfo;
		}
		
		
		//验证码不为空
		
		//判断是否是同一个号码，判断是否已经超时了一分钟
		
		if (!crcode.getMobile().equals(mobile)) {
			resultInfo.setCode("100017");
			resultInfo.setMessage("该验证码不是同一个手机号码");
			return resultInfo;
		}else {   //是同一个手机号码，判断是否超时
			if ((System.currentTimeMillis()-crcode.getSendDate().getTime())/1000/60>1) {
				resultInfo.setCode("100018");
				resultInfo.setMessage("验证码失效，请重新获取");
				return resultInfo;
			}else {    //验证验证码是否正确
				if (!crcode.getCrCode().equals(code)) {
					resultInfo.setCode("100019");
					resultInfo.setMessage("验证码不正确，请重新输入");
					return resultInfo;
				}else {
					try {
						Object object=userService.bindMobile(Integer.parseInt(userId), mobile);
						return object;
					} catch (Exception e) {
						resultInfo.setCode("100015");
						resultInfo.setMessage("绑定异常");
						return resultInfo;
					}
				}
			}
		}
		
		
	}
	
	
	/**
	 * 取消绑定手机或者邮箱
	 * @param userId  用户id
	 * @param type 类型 只能是email和mobile
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/unbindMobileOrEmail")
	public Object unbindMobileOrEmail(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String userId=request.getParameter("userId");
		String type=request.getParameter("type");
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(type)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("取绑类型不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(userId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("用户Id只能是数字");
			return resultInfo;
		}
		
		try {
			Object object=userService.unbindMobileOrEmail(Integer.parseInt(userId), type);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("取绑异常");
			return resultInfo;
		}
		
	}
	
	
	
	
	
	//发送邮件
	
	
	
	
	
	
	/**
	 * 绑定邮箱
	 * @param userId  用户id
	 * @param email  邮箱
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/bindEmail")
	public Object bindEmail(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String userId=request.getParameter("userId");
		String email=request.getParameter("email");
		
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(email)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("邮箱不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(userId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("用户Id格式不正确");
			return resultInfo;
		}
		
		if (!StringUtil.isEmail(email)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("邮箱格式不正确");
			return resultInfo;
		}
		
		
		try {
			Object object=userService.bindEmail(Integer.parseInt(userId), email);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("绑定邮箱异常");
			return resultInfo;
		}
	}
	
	
	
	
	
	
	
}
