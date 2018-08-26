package org.ruanwei.demo.springframework.dataAccess.orm.jpa.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Table(name="user")
@Entity
public class UserEntity {
	
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Id
	private String name;
	
	@Column(name="age")
	private int age;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="birthday")
	private Date birthday;

	public UserEntity() {
	}

	public UserEntity(String name, int age, Date birthday) {
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
	}*/

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", birthday=" + birthday
				+ "]";
	}

}
