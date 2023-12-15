package com.example.project_ee297.Service.ServiceImp;


import com.example.project_ee297.Controller.UserController;
import com.example.project_ee297.DAO.UserDAO;
import com.example.project_ee297.FaceCompare.WebFaceCompare;
import com.example.project_ee297.Object.Diary;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Object.User;
import com.example.project_ee297.Service.UserService;
import com.example.project_ee297.translate.TransApi;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;

;


@Component
public class UserServiceImp implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Result<User> register(User user) {
        Result<User> result = new Result<>();
        // 先去数据库找用户名是否存在
        User getUser = userDAO.getByUserAccount(user.getAccount());
        if (getUser != null) {
            result.setResultFailed("该用户名已存在！");
            return result;
        }
        // 存入数据库
        userDAO.add(user);
        // 返回成功消息
        result.setResultSuccess("注册用户成功！", user);
        return result;
    }

    @Override
    public Result<User> login(User user) {
        Result<User> result = new Result<>();
        // 去数据库查找用户
        User getUser = userDAO.getByUserAccount(user.getAccount());
        if (getUser == null) {
            result.setResultFailed("用户不存在！");
            return result;
        }
        if (!getUser.getPassword().equals(user.getPassword())) {
            result.setResultFailed("用户名或者密码错误！");
            return result;
        }
        // 设定登录成功消息
        result.setResultSuccess("登录成功！", getUser);
        String path=getUser.getHeadPortraitBase64Code();
        try {
            // 读取文件内容
            byte[] fileBytes = Files.readAllBytes(Path.of(path));
            getUser.setHeadPortraitBase64Code(Base64.getEncoder().encodeToString(fileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Result<User> update(User user) throws Exception {
        Result<User> result = new Result<>();
        // 去数据库查找用户
        User getUser = userDAO.getByUserAccount(user.getAccount());
        if (getUser == null) {
            result.setResultFailed("用户不存在！");
            return result;
        }
        // 检测传来的对象里面字段值是否为空，若是就用数据库里面的对象相应字段值补上
        if (!StringUtils.isEmpty(user.getPassword())) {

            user.setPassword(user.getPassword());
        }

        // 存入数据库
        userDAO.update(user);
        result.setResultSuccess("修改用户成功！", user);
        return result;
    }

    @Override
    public Result<User> isLogin(HttpSession session) {
        Result<User> result = new Result<>();
        // 从session中取出用户信息
        User sessionUser = (User) session.getAttribute(UserController.SESSION_NAME);
        // 若session中没有用户信息这说明用户未登录
        if (sessionUser == null) {
            result.setResultFailed("用户未登录！");
            return result;
        }
        // 登录了则去数据库取出信息进行比对
        User getUser = userDAO.getById(sessionUser.getId());
        // 如果session用户找不到对应的数据库中的用户或者找出的用户密码和session中用户不一致则说明session中用户信息无效
        if (getUser == null || !getUser.getPassword().equals(sessionUser.getPassword())) {
            result.setResultFailed("用户信息无效！");
            return result;
        }
        result.setResultSuccess("用户已登录！", getUser);

        return result;
    }
/*
    @Override
    public Result<User> updateImg(User user) throws Exception {
        Result<User> result = new Result<>();
        User getUser = userDAO.getByUserAccount(user.getAccount());
        if (getUser == null) {
            result.setResultFailed("用户不存在！");
            return result;
        }
        userDAO.updateImg(user);
        result.setResultSuccess("修改用户成功！", user);
        return result;
    }
    */
   @Override
    public Result<User> LoginByFace(User user)throws Exception{
        Result<User>  result=new Result<>();
        User getUser=userDAO.getByUserAccount(user.getAccount());
       if (getUser == null) {
           result.setResultFailed("用户不存在！");
           return result;
       }
       String projectRoot = System.getProperty("user.dir");
       String imagePath  = Paths.get(projectRoot, "resource/Head/face/currentImg.png").toString();
       String path= Paths.get(projectRoot, "resource/Head/face/"+user.getAccount()+".png").toString();
       byte[] imageBytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(user.getHeadPortraitBase64Code());
       File imageFile = new File(imagePath);
       try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
           outputStream.write(imageBytes);
       } catch (IOException e) {
           e.printStackTrace();
           result.setResultFailed("保存图片失败");
           return result;
       }
       WebFaceCompare webFaceCompare=new WebFaceCompare("https://api.xf-yun.com/v1/private/s67c9c78c",
               "c42a8fc5","NDRkOGEwYWQwMzkwYTM2OTc2M2RmODYx", "1389aa5d53e53ae7578609062dffe3b9",
               path,imagePath,"s67c9c78c");
       JSONObject jsonObject = new JSONObject(WebFaceCompare.similarity(imagePath,path));
       double score = jsonObject.getDouble("score");
       System.out.println("Score   = " +score);
       if(score>0.80){
          result.setResultSuccess("登录成功",getUser);
          getUser.setHeadPortraitBase64Code(user.getHeadPortraitBase64Code());
          return result;
       }else{
           result.setResultFailed("人像比对不通过");
           return result;
       }
   }


    @Override
    public String TestEmotion(Diary diary){
        String projectRoot = System.getProperty("user.dir");
        String testDiary  = Paths.get(projectRoot, "resource/sentimentAnalyse1/testDiary.txt").toString();

       String result = "";
       TransApi transApi=new TransApi("20231102001867835","9hQb6t9crP0Lx4eMyubX");
      String text= unicodeDecode( transApi.getTransResult(diary.getContent(),"en","zh"));
        System.out.println(diary.getContent());
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(testDiary), false))) {
           // 写入字符串
           writer.write(text);
       } catch (IOException e) {
           e.printStackTrace();
       }
        String pythonScriptPath= Paths.get(projectRoot, "resource/sentimentAnalyse1/predict.py").toString();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Exited with error code: " + exitCode);

            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Error: Python script execution failed.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(testDiary))) {
            // Read the first line of the file
            result = reader.readLine();
            LOGGER.info("result is: "+result);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately, e.g., log it or return an error to the client
            return "error";
        }

        if (result.contains("1")) {
            return "positive";
        } else {
            return "negative";
        }

    }

    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}