package com.work.productList.crud.insfrastructure;

import java.util.UUID;

public class IDGen {
	
	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
