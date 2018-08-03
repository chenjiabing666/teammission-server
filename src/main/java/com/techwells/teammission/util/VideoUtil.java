package com.techwells.teammission.util;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.util.List;

//import com.aliyuncs.sms.model.v20160927.QuerySmsDetailByPageResponse.stat;

public class VideoUtil {
	/**
	 * 获取视频的时长  格式：HH:MM:ss
	 * @param file 视频的文件
	 * @return
	 * @throws Exception
	 */
	public static String getVideoLength(File file) throws Exception{
		Encoder encoder=new Encoder();
		MultimediaInfo multimediaInfo=encoder.getInfo(file);  //获取MultimediaInfo，其中封装了一些file文件的信息
		Long mils=multimediaInfo.getDuration();  //获取视频的时长，毫秒单位
		int hour=(int) (mils/1000/3600);   //小时
		int minute=(int) ((mils/1000%3600)/60);   //分钟
		int second=(int) (mils/1000-hour*3600-minute*60);   //秒
		return hour+":"+minute+":"+second;
	}
	
	/**
	 * 获取视频的高度
	 * @param file  视频的文件对象
	 * @return
	 * @throws Exception
	 */
	public static int getHeight(File file) throws Exception{
		Encoder encoder=new Encoder();
		MultimediaInfo multimediaInfo=encoder.getInfo(file);  //获取MultimediaInfo，其中封装了一些file文件的信息
		return multimediaInfo.getVideo().getSize().getHeight();
	}
	
	
	/**
	 * 获取视频的宽度
	 * @param file  文件对象
	 * @return
	 * @throws Exception
	 */
	public static int getWidth(File file) throws Exception{
		Encoder encoder=new Encoder();
		MultimediaInfo multimediaInfo=encoder.getInfo(file);  //获取MultimediaInfo，其中封装了一些file文件的信息
		return multimediaInfo.getVideo().getSize().getWidth();
	}
	
	
	/**
	 * 截取视频的第一帧并且保存成图片
	 * @param veido_path  需要截取的视频路径
	 * @param ffmpeg_path   ffmpeg.ext的路径
	 * @return    第一帧图片的位置
	 */
	public static String processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
			return null;
		}
		String filepath=veido_path.substring(0, veido_path.lastIndexOf("."))
				.replaceFirst("vedio", "file") + ".jpg";
		System.out.println(filepath);
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpeg_path);
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add("8");// 这个参数是设置截取视频多少秒时的画面
		// commands.add("-t");
		// commands.add("0.001");
		commands.add("-s");
		commands.add("700x525");
		commands.add(veido_path.substring(0, veido_path.lastIndexOf("."))
				.replaceFirst("vedio", "file") + ".jpg");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			System.out.println("截取成功");
			return filepath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
