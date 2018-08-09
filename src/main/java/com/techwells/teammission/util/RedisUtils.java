package com.techwells.teammission.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.techwells.teammission.domain.User;

/**
 * redis的工具类
 * @author chenjiabing
 */
public class RedisUtils {
	private  RedisTemplate<String, Object> template;
	public final RedisTemplate<String, Object> getTemplate() {
		return template;
	}
	public final void setTemplate(RedisTemplate<String, Object> template) {
		this.template = template;
	}
	/**
	 * 向redis中添加对象,string类型的对象
	 * @param object   需要存储的对象
	 * @param key  存储的键
	 * @throws Exception  出现异常信息
	 */
	public  void addStringObject(String key,Object object)throws Exception{
		template.opsForValue().set(key,object);
	}
	
	/**
	 * 添加指定的key到Redis中
	 * @param key 指定的Ke
	 * @param object  数据
	 * @param timeout  过期时间
	 * @param unit    时间单位
	 * @throws Exception
	 */
	public void addStringObject(String key,Object object,Long timeout,TimeUnit unit) throws Exception{
		this.addStringObject(key, object);
		template.expire(key, timeout, unit);
	}
	
	/**
	 * 根据键值从redis中获取对象 string类型的对象
	 * @param key  key
	 * @return   返回对象
	 * @throws Exception 抛出的异常
	 */
	public  Object getStringObject(String key)throws Exception{
		Object object=template.opsForValue().get(key);
		return object;
	}
	
	/**
	 * 根据key删除指定的值
	 * @param key  key
	 * @throws Exception 异常信息
	 */
	public void deleteObject(String key)throws Exception{
		template.delete(key);
	}
	
	
	/**
	 * 批量删除对象
	 * @param keys key的集合
	 */
	public void deleteObjectBatch(Collection<String> keys)throws Exception{
		template.delete(keys);
	}
	/**
	 * 根据key更新值
	 * @param key  key
	 * @param object  value
	 * @throws Exception  异常信息
	 */
	public void modifyStringObject(String key,Object object)throws Exception{
		this.addStringObject(key, object);
	}

	
	/**
	 * 添加数据在Hash中
	 * @param key  key
	 * @param field  指定的域
	 * @param object  数据
	 */
	public void addHashObject(String key,String field,Object object)throws Exception{
		template.opsForHash().put(key, field, object);
	}
	
	
	/**
	 * 向hash中添加数据，并且设置过期的时间
	 * @param key  key
	 * @param field  域
	 * @param object  数据
	 * @param timeout  过期时间
	 * @param unit    单位
	 * @throws Exception
	 */
	public void addHashObject(String key,String field,Object object,Long timeout,TimeUnit unit)throws Exception{
		this.addHashObject(key, field, object);
		this.setExpireTimeForKey(key, timeout, unit);
	}
	
	
	/**
	 * 批量添加数据到指定的hash中
	 * @param key  key
	 * @param map  需要添加的数据  Map<field,value>
	 * @param expireTime  过期时间，单位秒,如果为null，默认永远不过期
	 */
	public void addHashObjectBatch(String key,Map<Object, Object> map,Long expireTime,TimeUnit unit)throws Exception{
		template.opsForHash().putAll(key,map);
		if (expireTime!=null) {
			this.setExpireTimeForKey(key, expireTime,unit);  //设置过期时间
		}
	}
	
	
	
	
	
	/**
	 * 为指定的key设置过期时间
	 * @param key  key
	 * @param timeout  过期时间
	 * @param unit   指定时间的单位
	 */
	public void setExpireTimeForKey(String key,Long timeout,TimeUnit unit){
		template.expire(key, timeout, unit);
	}
	
	/**
	 * 根据key，field从hash中获取数据
	 * @param key
	 * @param field
	 * @return  Object对象
	 */
	public Object getHashObject(String key,String field)throws Exception{
		return template.opsForHash().get(key, field);
	}
	
	
	/**
	 * 修改指定key，field中的数据
	 * @param key  
	 * @param field
	 * @param object
	 */
	public void modifyHashObject(String key,String field,Object object)throws Exception{
		this.addHashObject(key, field, object);
	}
	
	/**
	 * 删除指定的key和field中的数据
	 * @param key
	 * @param field
	 */
	public void deleteHashObject(String key,String field)throws Exception{
		this.deleteHashObjectBatch(key, new Object[]{field});
	}
	
	/**
	 * 根据key和fields批量获取其中的数据
	 * @param key  key
	 * @param fields  {@link Collection<Object> }
	 * @throws Exception
	 */
	public void getHashObjectBatch(String key,Collection<Object> fields)throws Exception{
		template.opsForHash().multiGet(key, fields);
	}
	
	/**
	 * 批量删除指定key和fields的数据
	 * @param key  key
	 * @param fields  需要删除的域
	 * @throws Exception
	 */
	public void deleteHashObjectBatch(String key,Object[] fields)throws Exception{
		template.opsForHash().delete(key,fields);
	}
	
	
	/**
	 * 添加数据到ZSet中
	 * @param key    指定的key
	 * @param value  指定的value
	 * @param score  指定的score
	 */
	public void addZSetObject(String key,String value,double score)throws Exception{
		template.opsForZSet().add(key, value, score);
	}
	
	
	/**
	 * 批量添加数据到Zset中
	 * @param key  指定的key
	 * @param typedTuple  {@link TypedTuple}
	 */
	public void addZSetObjectBatch(String key,Set<TypedTuple<Object>> typedTuple){
		template.opsForZSet().add(key, typedTuple);
	}
	
	/**
	 * 根据key获取start--end之间的数据
	 * @param key  指定key
	 * @param start 开始索引，从0开始
	 * @param end  结束索引
	 * @return  {@link Set<Object>}
	 */
	public Set<Object> getZSetObject(String key,Long start,Long end){
		return template.opsForZSet().range(key, start, end);
	}
	
	
	/**
	 * 根据Score的范围获取数据
	 * @param key  指定的key值
	 * @param min  score的最小值
	 * @param max score的最大值
	 * @return  {@link Set<Object>}
	 */
	public Set<Object> getZSetObjectRangeByScore(String key,Long min,Long max){
		return template.opsForZSet().rangeByScore(key, min, max);
	}
	
	/**
	 * 根据Score的范围获取数据,分页获取
	 * @param key  指定的key
	 * @param min  最小值
	 * @param max  最大值
	 * @param offset  偏移量
	 * @param count  数量
	 * @return
	 */
	public Set<Object> getZSetObjectRangeByScore(String key,Long min,Long max,Long offset,Long count){
		return template.opsForZSet().rangeByScore(key, min, max, offset, count);
	}
	
	/**
	 * 向List中添加元素，从表头添加
	 * @param key
	 * @param value
	 */
	public void addLeftListObject(String key,Object value){
		template.opsForList().leftPush(key, value);
	}
	
	/**
	 * 向List中添加元素，从表尾添加
	 * @param key
	 * @param value
	 */
	public void addRightListObject(String key,Object value){
		template.opsForList().rightPush(key, value);
	}
	
	/**
	 * 批量从表头添加数据
	 * @param key
	 * @param timeout ： 过期时间  如果为null表示永久不过期
	 * @param timeUnit : 时间单位
	 * @param values  {@link Collection<Object>}
	 */
	public void addLeftListObjectBatch(String key,Collection<Object> values,Long timeout,TimeUnit unit){
		template.opsForList().leftPushAll(key, values);
		if (timeout!=null) {
			this.setExpireTimeForKey(key, timeout, unit);
		}
	}
	
	/**
	 * 批量从表尾添加数据
	 * @param key
	 * @param values {@link Collection<Object>}
	 */
	public void addRigthListObjectBatch(String key,Collection<Object> values,Long timeout,TimeUnit unit){
		template.opsForList().rightPushAll(key, values);
		if (timeout!=null) {
			this.setExpireTimeForKey(key, timeout, unit);
		}
	}
	
	/**
	 * 获取指定范围内的数据
	 * @param key  
	 * @param i  开始的索引 从0开始
	 * @param j   结束的索引，-1 表示结尾
	 * @return   {@link List<Object>}
	 */
	public List<Object> getRangeListObject(String key,int i,int j){
		return template.opsForList().range(key, i, j);
	}
	
	
	/**
	 * 根据实体类的key和指定的查询参数、方法名称获取指定的key
	 * @param domainKey  实体类指定的key
	 * @param params  参数 Map<String,Object>
	 * @param functionName : 方法的名称
	 * @return
	 */
	public static String getRedisKey(String domainKey,String functionName,PagingTool pagingTool)throws Exception{
		StringBuilder builder=new StringBuilder();
		Map<String,Object> params=pagingTool.getParams();
		builder.append(domainKey+"_"+functionName+"_"+pagingTool.getStartNum()+"_"+pagingTool.getPageSize()+"_");
		for (String key : params.keySet()) {
			builder.append(key+"="+params.get(key)+"_");
		}
		return builder.toString();
	}
	
	/**
	 * 将Collection<?extend Object>的集合转换成Collection<Object>
	 * @param list 需要转换的集合
	 * @return   Collection<Object>
	 * @throws Exception
	 */
	public static Collection<Object> convertToCollection(Collection<? extends Object> list) throws Exception{
		List<Object> arrayList=new ArrayList<Object>(list.size());
		for (Object object : list) {
			arrayList.add(object);
		}
		return arrayList;
	}
	
	/**
	 * 将指定的List集合中的元素逆向
	 * @param objects  List<? extends Object>
	 */
	public static void reverse(List<? extends Object> objects){
		Collections.reverse(objects);
	}
	
	
	/**
	 * 删除所有的键值
	 */
	public void delteAllKeys(){
		Set<String> keys=template.keys("*");  //获取所有的key
		template.delete(keys);   //删除所有的键值
	}
	
}
