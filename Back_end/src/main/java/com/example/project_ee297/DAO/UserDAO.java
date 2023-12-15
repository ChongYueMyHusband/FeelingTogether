package com.example.project_ee297.DAO;

import com.example.project_ee297.Object.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO {

    @Insert("INSERT INTO user (account, password) VALUES (#{account}, #{password})")
    int add(User user);

    @Update("UPDATE user SET  password = #{password}, username = #{username} ,userIdiograph=#{userIdiograph}, age =#{age}, gender=#{gender},headPortraitBase64Code = #{headPortraitBase64Code}WHERE account = #{account}")
    int update(User user);

    @Update("UPDATE user SET headPortraitBase64Code = #{headPortraitBase64Code} WHERE account = #{account}")
    int updateImg(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(Integer id);

    @Select("SELECT * FROM user WHERE account = #{account}")
    User getByUserAccount(String account);


}
