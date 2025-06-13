package kr.co.myproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.User;
import kr.co.myproject.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public int insertUser(User user)
	{
		return userMapper.insertUser(user);
	}
	
	public User findByUsername(String username)
	{
		return userMapper.findByUsername(username);
	}

	public User findByIdx(int idx)
	{
		return userMapper.findByIdx(idx);
	}
	
	public String findWriter(String username)
	{
		return userMapper.findWriter(username);
	}

	public List<User> findAllUser()
	{
		return userMapper.findAllUser();
	}

	public int countByUsername(String username)
	{
		return userMapper.countByUsername(username);
	}

	public int updatePassword(User user)
	{
		return userMapper.updatePassword(user);
	}

	public int UpdateWriter(User user)
	{
		return userMapper.UpdateWriter(user);
	}

	public int UpdatePassword(User user)
	{
		return userMapper.UpdatePassword(user);
	}

	public int UpdateBan(boolean ban, int idx)
	{
		return userMapper.UpdateBan(ban, idx);
	}
}
