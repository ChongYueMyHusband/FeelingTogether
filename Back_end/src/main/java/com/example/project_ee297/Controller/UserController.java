package com.example.project_ee297.Controller;

import com.example.project_ee297.Object.Diary;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.User;
import com.example.project_ee297.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;


@RestController
public class UserController {

    public static final String SESSION_NAME = "userInfo";
    @Autowired
    private  UserService userService;
    private BindingResult errors ;
    /**
     * 用户注册
     *
     * @param user    传入注册用户信息
     * @param errors  Validation的校验错误存放对象
     * @param request 请求对象，用于操作session
     * @return 注册结果
**/
    @PostMapping("register")
    public Result<User> register(@RequestBody @Valid User user, BindingResult errors, HttpServletRequest request) {
        LOGGER.info("收到一条注册");
        Result<User> result;
        // 如果校验有错，返回注册失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            result.setResultFailed(errors.getFieldError().getDefaultMessage());
            return result;
        }
        // 调用注册服务
        result = userService.register(user);
        return result;
    }
/*

    @SneakyThrows
    @RequestMapping("register")
    public void register(@Valid User user,BindingResult errors) throws MqttException {
        mqttserver.init("pub_register",1);
        MqttMessage data=mqttserver.message;
        String message = new String(data.getPayload(),"UTF-8");
        // 使用Jackson库将JSON字符串转换为User对象
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(message, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(user.getUsername());
        Result<User> result;
        // 如果校验有错，返回注册失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            result.setResultFailed(errors.getFieldError().getDefaultMessage());
            System.out.println("receive failed");
            return ;
        }
        // 调用注册服务
        result = userService.register(user);
        System.out.println("receive correctly ");
        mqttserver.sendMQTTMessage("pub_register",result.getMessage(),0);
        return;
    }

    @SneakyThrows
    @PostMapping("/login")
    public Result<User> login(@RequestBody @Valid User user, BindingResult errors, HttpServletRequest request) {
        mqttserver.init("pub_account", 0);
        Result<User> result;
        MqttMessage data=mqttserver.message;
        String message = new String(data.getPayload(),"UTF-8");
        // 使用Jackson库将JSON字符串转换为User对象
        ObjectMapper objectMapper = new ObjectMapper();
        user = objectMapper.readValue(message, User.class);
        // 如果校验有错，返回登录失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            result.setResultFailed(errors.getFieldError().getDefaultMessage());
            mqttServer.sendMQTTMessage("pub_account", result.getMessage(), 0);
            return result;
        }
        // 调用登录服务
        result = userService.login(user);
        // 如果登录成功，则设定session
        if (result.isSuccess()) {
            request.getSession().setAttribute(SESSION_NAME, result.getData());
        }
        String ReturnMessage= "id="+result.getData().getUsername()+"&password="+result.getData().getPassword()+"&idiograph="
                +result.getData().getUserIdiograph()+"&sex="+result.getData().getGender()+"&age="+result.getData().getAge()+
               " &avatar="+result.getData().getHeadPortraitBase64Code();

        mqttServer.sendMQTTMessage("pub_account",ReturnMessage,0);
        return result;
    }

    @PostMapping("/updateImg")
    public Result<User> updateImg(@RequestBody User user, HttpServletRequest request) throws Exception {
        mqttserver.init("pub_Img", 0);
        MqttMessage data=mqttserver.message;
        String message = new String(data.getPayload(),"UTF-8");

        // 使用Jackson库将JSON字符串转换为User对象
        ObjectMapper objectMapper = new ObjectMapper();
        user = objectMapper.readValue(message, User.class);

        Result<User> result = new Result<>();
        HttpSession session = request.getSession();
        // 检查session中的用户（即当前登录用户）是否和当前被修改用户一致
        User sessionUser = (User) session.getAttribute(SESSION_NAME);
        if (sessionUser.getId() != user.getId().intValue()) {
            result.setResultFailed("当前登录用户和被修改用户不一致，终止！");
            mqttServer.sendMQTTMessage("pub_personalInfo", result.getMessage(), 0);
            return result;
        }
        result = userService.update(user);
        // 修改成功则刷新session信息
        if (result.isSuccess()) {
            session.setAttribute(SESSION_NAME, result.getData());
        }
        mqttServer.sendMQTTMessage("pub_personalInfo", result.getMessage(), 0);
        return result;
    }
*/

    @PostMapping("/login")
    public Result<User> login(@RequestBody @Valid User user, BindingResult errors) {
        LOGGER.info("收到一条登录");
        Result<User> result;
        // 如果校验有错，返回登录失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            result.setResultFailed(errors.getFieldError().getDefaultMessage());
            return result;
        }
        // 调用登录服务
        result = userService.login(user);
        return result;
    }

    /**
     * 判断用户是否登录
     *
     * @param request 请求对象，从中获取session里面的用户信息以判断用户是否登录
     * @return 结果对象，已经登录则结果为成功，且数据体为用户信息；否则结果为失败，数据体为空
     */
    @GetMapping("/is-login")
    public Result<User> isLogin(HttpServletRequest request) {
        // 传入session到用户服务层
        return userService.isLogin(request.getSession());
    }

    /**
     * 用户信息修改
     *
     * @param user    修改后用户信息对象
     * @param request 请求对象，用于操作session
     * @return 修改结果
     */
    @PutMapping("/PersonalInfo")
    public Result<User> update(@RequestBody User user, HttpServletRequest request) throws Exception {
        LOGGER.info("收到一条更新信息");
        Result<User> result = new Result<>();
        String Base64Code= user.getHeadPortraitBase64Code();
        String Path="E:/Head/"+user.getAccount()+".png";
        byte[] imageBytes = Base64.decodeBase64(Base64Code);
        File imageFile = new File(Path);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
            outputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            result.setResultFailed("Failed");
            return result;
        }
        user.setHeadPortraitBase64Code(Path);
        HttpSession session = request.getSession();
        result = userService.update(user);
        // 修改成功则刷新session信息
        if (result.isSuccess()) {
            session.setAttribute(SESSION_NAME, result.getData());
        }
        user.setHeadPortraitBase64Code(Base64Code);
        return result;
    }
   @SneakyThrows
   @PostMapping("/updateFace")
    public Result<User> updateHeadPortrait(@RequestBody  User user) {
        LOGGER.info("收到一条面部信息");
        Result<User> result=new Result<>();
       String Path="E:/Head/face/"+user.getAccount()+".png";
       String Base64Code= user.getHeadPortraitBase64Code();
       byte[] imageBytes = Base64.decodeBase64(Base64Code);
       File imageFile = new File(Path);
       try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
           outputStream.write(imageBytes);
           result.setResultSuccess("保存成功");
           return result;
       } catch (IOException e) {
           e.printStackTrace();
           result.setResultFailed("保存失败");
           return result;
       }

    }
    @SneakyThrows
    @PostMapping("/loginByFace")
    public Result<User> compareFace(@RequestBody  User user) {
        LOGGER.info("收到一条面部登录");
        return userService.LoginByFace(user);
    }


    @PostMapping("/Diary")
    public String PredictEmo(@RequestBody Diary diary){
        LOGGER.info("收到一条待测日记");
      return userService.TestEmotion(diary);
    }



    /**
     * 用户登出
     *
     * @param request 请求，用于操作session
     * @return 结果对象
     */
    @GetMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        Result<Void> result = new Result<>();
        // 用户登出很简单，就是把session里面的用户信息设为null即可
        request.getSession().setAttribute(SESSION_NAME, null);
        result.setResultSuccess("用户退出登录成功！");
        return result;
    }

}