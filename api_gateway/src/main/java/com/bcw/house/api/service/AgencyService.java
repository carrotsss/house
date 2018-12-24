package com.bcw.house.api.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcw.house.api.common.PageData;
import com.bcw.house.api.common.PageParams;
import com.bcw.house.api.dao.UserDao;
import com.bcw.house.api.model.Agency;
import com.bcw.house.api.model.ListResponse;
import com.bcw.house.api.model.User;


@Service
public class AgencyService {
  
  @Autowired
  private UserDao userDao;
   
  
  public List<Agency> getAllAgency(){
    return userDao.getAllAgency();
  }

  public Agency getAgency(Integer id){
    return userDao.getAgencyById(id);
  }

  public void add(Agency agency) {
      userDao.addAgency(agency);
  }

  public PageData<User> getAllAgent(PageParams pageParams) {
    ListResponse<User> result =  userDao.getAgentList(pageParams.getLimit(),pageParams.getOffset());
                  Long  count  =  result.getCount();
    return PageData.<User>buildPage(result.getList(), count, pageParams.getPageSize(), pageParams.getPageNum());
  }

  
  
  public User getAgentDetail(Long id) {
    return userDao.getAgentById(id);
  }
  
  
  
  
}
