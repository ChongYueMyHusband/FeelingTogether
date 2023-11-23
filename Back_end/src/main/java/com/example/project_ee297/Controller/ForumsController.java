package com.example.project_ee297.Controller;

import com.example.project_ee297.Mqtt.MQTTServer;
import com.example.project_ee297.Object.Post;
import com.example.project_ee297.Object.Result;
import com.example.project_ee297.Service.ForumsService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.hibernate.metamodel.mapping.MappingModelCreationLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;

@RestController
public class ForumsController {


    @Autowired
    private  ForumsService forumsService;
    @Autowired
    private  MQTTServer mqttServer;
    private MqttMessage mqttMessage;

    private  JdbcTemplate jdbcTemplate;

    @Autowired
    public  ForumsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/Post")
    public Result<Post> update(@RequestBody Post post) throws Exception{
        LOGGER.info("收到一条帖子上传");
        Result<Post> result = new Result<>();
        Timestamp time=new Timestamp(System.currentTimeMillis());
        post.setLike(0);
        post.setRemark(0);
        post.setTimestamp(time);
        result = forumsService.posting(post);
       return  result;
    }
    /*
    @SneakyThrows
    @PostMapping("/Post")
    public void Post(String topic) throws MqttException {
          mqttServer.init("pub_forum",1);
           mqttMessage=mqttServer.getMessage();
            String message = new String(mqttMessage.getPayload());
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(message, Post.class);
            Result<Post> result = new Result<>();
            if(error.hasErrors()) {
               result.setResultFailed(error.getFieldError().getDefaultMessage());
                mqttServer.sendMQTTMessage("pub_account", result.getMessage(), 0);

            }
            result = forumsService.posting(post);
        mqttServer.sendMQTTMessage("pub_forum",result.getMessage(),0);
    }*/

    @PostMapping ("/getPostsInfo")
    public String getHistory(@RequestBody Post post) {
        MappingModelCreationLogger.LOGGER.info("发送帖子信息");
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT user_account, content, `like`, remark, created_at FROM posts ORDER BY id ASC");
        return results.toString();
    }

    @PostMapping("/like")
    public Result<Post> like(@RequestBody Post post){
        MappingModelCreationLogger.LOGGER.info("收到一条 点赞或取消点赞");
        Result<Post>result=new Result<>();
        result=forumsService.Like(post);
        return result;
    }

/*
    @PostMapping("/DeletePost")
    public  void DeletePost(String topic)throws  MqttException{
        mqttClient.subscribe(topic,(topic6,data)->{
            String message = new String(data.getPayload());
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(message, Post.class);
            Result<Post> result = new Result<>();
            result=forumsService.delete(post);
            MqttMessage mqttMessage=new MqttMessage();
            String ans="Delete target post successfully ";
            mqttMessage.setPayload(ans.getBytes(StandardCharsets.UTF_8));
            mqttClient.publish("pub_forum",mqttMessage);
        });
    }*/
}
