package com.mooc.house.web.interceptor;

import com.mooc.house.common.model.User;

public class UserContext {
	private static final ThreadLocal<User> USER_HODLER = new ThreadLocal<>();
    
	private static final ThreadLocal<User> user_houder = new ThreadLocal<User>();
	public static void setUser(User user){
		USER_HODLER.set(user);
	}
	
	public static void remove(){
		USER_HODLER.remove();
	}
	
	public static User getUser(){
		return USER_HODLER.get();
	}
}
