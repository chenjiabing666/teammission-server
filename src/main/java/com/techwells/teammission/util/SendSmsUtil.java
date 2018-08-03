package com.techwells.teammission.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendSmsUtil {
	// 短息产品参数
	public static final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
	public static final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
	// AK
	public static final String accessKeyId = "LTAIakyWTN6EtqOj";// 你的accessKeyId
	public static final String accessKeySecret = "CrbxOifuquQyGiu8KEsCb2xXmKvh5X";// 你的accessKeySecret
	public static final String sigaName = "阿里云短信测试专用";

	/**
	 * 生成六位随机数
	 * 
	 * @param args
	 */
	public static String getFourRadam() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			String s1 = String.valueOf((Math.random() * 9 + 1) * 10);
			stringBuffer.append(s1.substring(0, 1));
		}
		System.out.println(stringBuffer);
		return stringBuffer.toString();
	}

	/**
	 * 发送短信验证码 用户注册的时候发送验证码
	 * 
	 * @param mobile
	 *            手机号码
	 * @param crCode
	 *            短信验证码
	 * @throws Exception
	 */
	public static void sendUserCrCode(String mobile, String crCode)
			throws Exception {

		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数

		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				accessKeyId, accessKeySecret);

		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
				domain);

		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();

		// 使用post提交
		request.setMethod(MethodType.POST);

		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(mobile);

		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(sigaName);

		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode("SMS_139860239");

		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\"" + crCode + "\"}");

		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;

		sendSmsResponse = acsClient.getAcsResponse(request);

		if (sendSmsResponse.getCode() != null
				&& sendSmsResponse.getCode().equals("OK")) {
			System.out.println("发送成功");
		} else { // 短信发送给失败，抛出异常
			throw new Exception();
		}
	}
	
	/**
	 * 发送短信验证码   
	 * 修改密码的时候调用
	 * @param mobile  手机号码
	 * @param crCode  验证码
	 * @throws Exception
	 */
	public static void sendCodeByModifyPassword(String mobile, String crCode) throws Exception {

		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数

		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				accessKeyId, accessKeySecret);

		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
				domain);

		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();

		// 使用post提交
		request.setMethod(MethodType.POST);

		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(mobile);

		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(sigaName);

		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode("SMS_139860238");

		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\"" + crCode + "\"}");

		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;

		sendSmsResponse = acsClient.getAcsResponse(request);

		if (sendSmsResponse.getCode() != null
				&& sendSmsResponse.getCode().equals("OK")) {
			System.out.println("发送成功");
		} else { // 短信发送给失败，抛出异常
			throw new Exception();
		}
	}

	/**
	 * 发送用户名和密码给客户
	 * 
	 * @param mobile
	 * @param crCode
	 */
	public static void sendUserNameAndPassword(String mobile, String username,
			String password) {

		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数

		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				accessKeyId, accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
					domain);
		} catch (ClientException e1) {
			e1.printStackTrace();
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();

		// 使用post提交
		request.setMethod(MethodType.POST);

		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(mobile);

		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(sigaName);

		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode("SMS_140731300");

		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
//		request.setTemplateParam("{\"code\":\"" + username + "\"}");
		request.setTemplateParam("{\"name\":\""+username+"\", \"password\":\""+password+"\"}");

		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if (sendSmsResponse.getCode() != null
					&& sendSmsResponse.getCode().equals("OK")) {
				// 请求成功
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}