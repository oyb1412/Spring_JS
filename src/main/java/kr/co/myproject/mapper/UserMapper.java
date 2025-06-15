package kr.co.myproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.myproject.entity.User;

@Mapper
public interface UserMapper {
	
	@Insert("INSERT INTO springboot_project_study.user(username,password,writer,role,indate) VALUES(#{username}, #{password}, #{writer}, #{role}, #{indate})")
	public int insertUser(User user);
	
	@Select("SELECT idx,username,password,writer,role,ban FROM springboot_project_study.user WHERE username=#{username}")
	public User findByUsername(String username);

	@Select("SELECT idx,username,password,writer,role,ban FROM springboot_project_study.user WHERE idx=#{idx}")
	public User findByIdx(int idx);
	
	@Select("SELECT writer FROM springboot_project_study.user WHERE username=#{username}")
	public String findWriter(String username);

	@Select("SELECT COUNT(*) FROM springboot_project_study.user WHERE username = #{username}")
	public int countByUsername(String username);

	@Select("SELECT idx, username, writer, ban, indate FROM springboot_project_study.user")
	public List<User> findAllUser();
		
	@Update("UPDATE springboot_project_study.user SET password = #{password} WHERE username = #{username}")
	public int updatePassword(User user);

	@Update("UPDATE springboot_project_study.user SET writer = #{writer} WHERE username =#{username}")
	public int UpdateWriter(User user);

	@Update("UPDATE springboot_project_study.user SET password = #{password} WHERE username =#{username}")
	public int UpdatePassword(User user);

	@Update("UPDATE springboot_project_study.user SET ban = #{ban} WHERE idx =#{idx}")
	public int UpdateBan(@Param("ban") boolean ban, @Param("idx") int idx);
}
