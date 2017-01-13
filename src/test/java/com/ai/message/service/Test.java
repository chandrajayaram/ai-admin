package com.ai.message.service;

public class Test {
	public static void main(String[] args) {
		String soapAction ="http://www.caiso.com/soa/receivePCAPnodeClearing_v2";
		System.out.println(soapAction.substring(soapAction.lastIndexOf("/") + 1));
	}
}
