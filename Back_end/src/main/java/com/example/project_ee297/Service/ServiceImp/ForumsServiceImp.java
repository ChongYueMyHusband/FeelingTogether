package com.example.project_ee297.Service.ServiceImp;

import com.example.project_ee297.DAO.ForumsDAO;
import com.example.project_ee297.DAO.remarkDAO;
import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Service.ForumsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ForumsServiceImp implements ForumsService {
    @Autowired
    private ForumsDAO forumsDAO;
    @Autowired
    private remarkDAO remarkDAO;

    @Override
    public Result<Post> posting(Post post) {
        Result<Post> result = new Result<>();
        forumsDAO.addPost(post);
        result.setResultSuccess("发布成功！", post);
        return result;
    }
    @Override
    public Result<Post> delete(Post post){
        Result<Post> result=new Result<>();
        Post GetPost=forumsDAO.getById(post.getId());
        if(GetPost==null){
            result.setResultFailed("帖子不存在");
            return result;
        }
        forumsDAO.deletePostByIdAndUserAccount(post.getId(),post.getUser_account());
        return  result;
    }
    @Override
    public List<Post> getAllPosts(){
        return forumsDAO.getAllPosts();
    }

    @Override
    public Result<Post> Like(Post post) {
        Result<Post> result = new Result<>();
        Post getPost = forumsDAO.getById(post.getId());
        String LikeList = getPost.getLikeUserAccount();

        if (LikeList == null) {
            LikeList = post.getUser_account() + ",";
            getPost.setLikeUserAccount(LikeList);
            getPost.setLike(getPost.getLike() + 1);
            forumsDAO.updateLikeUserAccount(getPost);
            result.setResultSuccess("点赞成功");
            return result;
        }
        String pattern = "(?<=^|,)\\s*" + Pattern.quote(post.getUser_account()) + "\\s*(?=$|,)";
        Matcher matcher = Pattern.compile(pattern).matcher(LikeList);
        if (matcher.find()) {
            String deleteLikeUserAccount = matcher.replaceAll("").replaceAll("^,|,$|(?<=,)\\s*,\\s*(?=,)", "");
            // 用户已点过赞，取消点赞
            if (!LikeList.equals(deleteLikeUserAccount)) {
                getPost.setLikeUserAccount(deleteLikeUserAccount);
                getPost.setLike(getPost.getLike() - 1);
                forumsDAO.updateLikeUserAccount(getPost);
                result.setResultSuccess("取消点赞成功");
            } else {
                String addLikeUserAccount = LikeList + post.getUser_account() + ",";
                getPost.setLikeUserAccount(addLikeUserAccount);
                getPost.setLike(getPost.getLike() + 1);
                forumsDAO.updateLikeUserAccount(getPost);
                result.setResultSuccess("点赞成功");
            }
        } else {
            // 用户未点过赞，添加点赞
            String addLikeUserAccount = LikeList + post.getUser_account() + ",";
            getPost.setLikeUserAccount(addLikeUserAccount);
            getPost.setLike(getPost.getLike() + 1);
            forumsDAO.updateLikeUserAccount(getPost);
            result.setResultSuccess("点赞成功");
        }

        return result;
    }

}
