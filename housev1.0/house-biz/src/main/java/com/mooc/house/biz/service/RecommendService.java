package com.mooc.house.biz.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.arjuna.ats.internal.jdbc.drivers.modifiers.extensions;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mooc.house.common.model.House;
import com.mooc.house.common.page.PageParams;

import redis.clients.jedis.Jedis;

@Service
public class RecommendService {

  private static final String HOT_HOUSE_KEY = "hot_house";

  private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);

  @Autowired
  private HouseService houseService;

//  public void increase(Long id) {
//    try {
//      Jedis jedis = new Jedis("127.0.0.1");
//      jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id + "");
//      jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);// 0代表第一个元素,-1代表最后一个元素，保留热度由低到高末尾10个房产
//      jedis.close();
//    } catch (Exception e) {
//      logger.error(e.getMessage(),e);
//    }
//  }

  public void increase(Long id){
	  try{
		  Jedis jedis = new Jedis("127.0.0.1");
		  //对hot_house的key 为id的值加1
		  jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id +"");
		  //把值最小到到第11小的删掉，避免hot_house里的id过多
		  jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);
		  jedis.close();
	  }catch(Exception e){
		  logger.error(e.getMessage());
	  }
  }
//  public List<Long> getHot() {
//    try {
//      Jedis jedis = new Jedis("127.0.0.1");
//      Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
//      jedis.close();
//      List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
//      return ids;
//    } catch (Exception e) {
//      logger.error(e.getMessage(), e);//有同学反应在未安装redis时会报500,在这里做下兼容,
//      return Lists.newArrayList();
//    }
//
//  }

//  @Test
  public List<Long> getHot(){
//	public void getHot(){
	  try{
		  Jedis jedis = new Jedis("127.0.0.1");
		  //先倒序排列，返回下标为0，1的集合
		  Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, 1);
		  jedis.close();
		  List<Long> ids =idSet.stream().map(Long::parseLong).collect(Collectors.toList());
		  return ids;
	  }catch(Exception e) {
		  logger.error(e.getMessage());
		  return Lists.newArrayList();
	  }
  }
  
//  public List<House> getHotHouse(Integer size) {
//    House query = new House();
//    List<Long> list = getHot();
//    list = list.subList(0, Math.min(list.size(), size));
//    if (list.isEmpty()) {
//      return Lists.newArrayList();
//    }
//    query.setIds(list);
//    final List<Long> order = list;
//    List<House> houses = houseService.queryAndSetImg(query, PageParams.build(size, 1));
//    Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> {
//      return order.indexOf(hs.getId());
//    });
//    return houseSort.sortedCopy(houses);
//  }
  
  public List<House> getHotHouse(Integer size){
	  House query = new House();
	  List<Long> list = getHot();
	  list = list.subList(0, Math.min(list.size(), size));
	  if(list.isEmpty()){
		  return Lists.newArrayList();
	  }
	  query.setIds(list);
	  final List<Long> order = list;
	  List<House> houses = houseService.queryAndSetImg(query, PageParams.build(size,1));
	/**
	 * 对list里的House进行排序
	 * 该方法使用自然排序规则生成排序器，如从小到大、日期先后顺序。使用这个方法之前先介绍一下onResultOf 方法，
	 * 这个方法接收一个Function函数，该函数的返回值可以用于natural方法排序的依据，即根据这个返回值来进行自然排序，
	 */
	  Ordering<House> houseSort = Ordering.natural().onResultOf(
//	  hs -> {
//		 return order.indexOf(hs.getId()); 
//	  }
	  new Function<House, Comparable>() {  
		    @Override
		    public Comparable apply(House house) {  
		        return order.indexOf(house.getId());  
		    }
		}
	  );
//	  sortedCopy方法会使用创建的排序器排序并生成一个新的List
	  return houseSort.sortedCopy(houses);
  }

  
//  public List<House> getLastest() {
//    House query = new House();
//    query.setSort("create_time");
//    List<House> houses = houseService.queryAndSetImg(query, new PageParams(8, 1));
//    return houses;
//  }
  public List<House> getLastest(){
	  House query = new House();
	  //查询根据时间排序
	  query.setSort("create_time");
	  List<House> houses = houseService.queryAndSetImg(query, new PageParams(8,1));
	  return houses;
  }
  
  public static void main(String[] args) {
	Jedis jedis = new Jedis("127.0.0.1");
	jedis.zincrby("test_key", 1.0D, 1 + "");
  }
}
