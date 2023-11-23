package com.example.project_ee297.DAO;

import com.example.project_ee297.Object.remark;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface remarkDAO {
    @Insert("INSERT INTO remark (user_account, content, post_id) VALUES (#{user_account}, #{content}, #{post_id})")
    int addRemark(remark remark);

    @Delete("DELETE FROM remark WHERE id = #{id} AND user_account = #{user_account}")
    int deleteRemarkByIdAndUserAccount(@Param("id")int id, @Param("user_account)") String user_account);

    @Select("SELECT * FROM remark where post_id = #{post_id}")
    List<remark> getAllRemark(int post_id);

}
