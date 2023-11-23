package com.example.project_ee297.Service;

import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ForumsService {
         Result<Post> posting(@Valid Post post);

         Result<Post> delete(@Valid Post post);

         List<Post> getAllPosts();


    Result<Post> Like(Post post);
}
