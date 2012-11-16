package com.lyncode.oai.proxy.model.dao.api;

import java.util.List;

import com.lyncode.oai.proxy.model.entity.User;

public interface UserDao {
	public int saveUser(User user);
	public List<User> getAllUser(User user);
	public User selectUserById(int userId);
	public User selectUserByEmail(String email);
	public User selectUserByActivationKey(String key);
	public void deleteUser(User user);
	public String generateActivationKey();
}
