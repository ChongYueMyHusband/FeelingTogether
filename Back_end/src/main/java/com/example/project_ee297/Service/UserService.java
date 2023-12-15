package com.example.project_ee297.Service;

import com.example.project_ee297.Object.Diary;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 注册结果
     */
    Result<User> register(@Valid User user);

    /**
     * 用户登录
     *
     * @param user 用户对象
     * @return 登录结果
     */
    Result<User> login(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户对象
     * @return 修改结果
     */
    Result<User> update(User user) throws Exception;

    /**
     * 判断用户是否登录（实际上就是从session取出用户id去数据库查询并比对）
     *
     * @param session 传入请求session
     * @return 返回结果，若用户已登录则返回用户信息
     */
    Result<User> isLogin(HttpSession session);



    Result<User> LoginByFace(User user)throws Exception;


    String TestEmotion(Diary diary);
}