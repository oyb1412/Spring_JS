package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.CustomUser;
import kr.co.myproject.entity.User;
import kr.co.myproject.mapper.UserMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.findByUsername(username);
		
		if(user == null)
		{
			throw new UsernameNotFoundException(username +"존재하지 않습니다");
		}

		return new CustomUser(user);
	}

}
