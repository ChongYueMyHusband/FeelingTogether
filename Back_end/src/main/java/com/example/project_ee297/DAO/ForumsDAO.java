package com.example.project_ee297.DAO;

import com.example.project_ee297.Object.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ForumsDAO {
    @Insert("INSERT INTO posts (user_account, content, created_at, `like`, remark) VALUES (#{user_account}, #{content}, #{timestamp},#{like},#{remark})")
    int addPost(Post post);

    @Update("UPDATE posts SET LikeUserAccount = #{LikeUserAccount}, `like` = #{like} WHERE id = #{id}")
    int updateLikeUserAccount(Post post);


    @Update("UPDATE posts SET  remark= #{remark} WHERE id = #{id}")
    int remark (Post post);

    @Delete("DELETE FROM posts WHERE id = #{postId} AND user_account = #{user_account}")
    int deletePostByIdAndUserAccount(@Param("postId")int postId, @Param("user_account)") String user_account);

    @Select("SELECT * FROM posts WHERE id = #{id}")
    Post getById(Integer id);

    List<Post> getAllPosts();
}

