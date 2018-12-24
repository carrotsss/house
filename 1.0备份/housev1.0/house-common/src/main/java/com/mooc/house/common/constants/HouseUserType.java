package com.mooc.house.common.constants;

public enum HouseUserType {

	SALE(1),BOOKMARK(2);
	
	public final Integer value;
	
	private HouseUserType(Integer value){
		this.value = value;
	}
	
	public static void main(String[] args) {
		System.out.println(HouseUserType.BOOKMARK);;
	}
}
