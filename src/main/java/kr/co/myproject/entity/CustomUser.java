package kr.co.myproject.entity;

import org.springframework.security.core.authority.AuthorityUtils;

public class CustomUser extends org.springframework.security.core.userdetails.User {
	private final User user;

	public CustomUser(User user)
	{
		super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	@Override
	public boolean isEnabled() {
		return !user.isBan(); // ban이 true면 로그인 막음
	}

	// 필요하면 user 객체 그대로 꺼내서 쓰기도 가능
	public User getUser() {
		return user;
	}
}
