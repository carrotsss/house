package com.bcw.house.hsrv.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bcw.house.hsrv.model.City;

@Mapper
public interface CityMapper {
  
  public List<City> selectCitys(City city);

}
