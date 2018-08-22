package org.ruanwei.demo.springframework.data.jdbc;

import java.sql.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;

@Data
public class User2 {
	@Id
	private int id;
	private String name;
	private int age;
	private Date birthday;

	public User2() {
	}

	public User2(String name, int age, Date birthday) {
		this.name = name;
		this.age = age;
		this.birthday = birthday;
	}

	/*public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User2 [id=" + id + "name=" + name + ", age=" + age
				+ ", birthday=" + birthday + "]";
	}*/

}
