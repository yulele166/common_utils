package com.peanut.commons.utils.constants;

import java.io.Serializable;

/**
 * @author ming.yang
 * @since 2015年11月23日 下午3:24:09
 */
public class Student {
	@Override
	public String toString() {
		return "Student [username=" + username + ", age=" + age + "]";
	}

	private String username;
	private int age;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
