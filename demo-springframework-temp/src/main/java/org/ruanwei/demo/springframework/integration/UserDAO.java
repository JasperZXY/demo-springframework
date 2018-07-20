package org.ruanwei.demo.springframework.integration;

import java.util.List;

public interface UserDAO {

	public int countAll();

	public String findNameById(int id);
	
	public List<String> findNameList();

	public User findUserById(int id);

	public List<User> findUserList();

	public int insertUser(User user);

	public int updateUser(User user);

	public int[] batchUpdateUser(final List<User> users);

	public int deleteUser(int id);

	public void callProcedure();

	public void createTable();

}
