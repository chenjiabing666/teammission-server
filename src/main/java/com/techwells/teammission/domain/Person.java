package com.techwells.teammission.domain;

import java.io.Serializable;
import java.util.Date;



public class Person implements Serializable {
	private static final long serialVersionUID = 8628733012444843311L;
	private String name;
	private int age;
	private String email;
	private Date birthday;
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final int getAge() {
		return age;
	}
	public final void setAge(int age) {
		this.age = age;
	}
	public final String getEmail() {
		return email;
	}
	public final void setEmail(String email) {
		this.email = email;
	}
	public final Date getBirthday() {
		return birthday;
	}
	public final void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", email=" + email
				+ ", birthday=" + birthday + "]";
	}
	
}
