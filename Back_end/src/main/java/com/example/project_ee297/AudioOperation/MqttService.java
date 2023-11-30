package com.example.project_ee297.AudioOperation;
/*
import com.example.project_ee297.Audio.VoiceprintRecognition;
import com.example.project_ee297.AudioOperation.Convert;
import com.example.project_ee297.DAO.UserRepository;
import com.example.project_ee297.Model.User;
import com.example.project_ee297.FaceCompare.WebFaceCompare;
import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MqttService {

    @Autowired
    private MqttClient mqttClient;
    @Autowired

    private UserRepository userRepository;
    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(topic, mqttMessage);
    }
    public ResponseEntity<?> acceptAccount(String topic) throws MqttException {
        AtomicReference<ResponseEntity<?>> response = new AtomicReference<>(null); // 用于存储结果的原子引用
        mqttClient.subscribe(topic, (topic5, accountMessage) -> {
            String accountData = accountMessage.toString();
            String[] accountInfo = accountData.split("&");
            if (accountInfo.length != 2) {
                response.set(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                return;
            }
            String username = accountInfo[0];
            String password = accountInfo[1];
            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                response.set(ResponseEntity.status(HttpStatus.CONFLICT).build());
                return;
            }
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userRepository.save(newUser);
            response.set(ResponseEntity.ok().build());
        });

        return response.get();
    }
    String audioPathWave = "C:\\Users\\Lenovo\\Music\\Audio\\CurrentAudio.wave";
    String audioPathMp3 = "C:\\Users\\Lenovo\\Music\\Audio\\CurrentAudio.mp3";
    String imagePath = "C:\\Users\\Lenovo\\Pictures\\Screenshots\\currentImage.jpg";
    String filePath="C:\\Users\\Lenovo\\Pictures\\Screenshots\\savedImage.jpg";
    public void subscribeImg(String topic) throws MqttException {
        mqttClient.subscribe(topic, (topic1, message) -> {
            String payload = new String(message.getPayload());
            System.out.println("Received message: " + payload);
            WebFaceCompare webFaceCompare=new WebFaceCompare("https://api.xf-yun.com/v1/private/s67c9c78c","c42a8fc5","NDRkOGEwYWQwMzkwYTM2OTc2M2RmODYx", "1389aa5d53e53ae7578609062dffe3b9",imagePath,filePath,"s67c9c78c");
            byte[] imageBytes = Base64.decodeBase64(String.valueOf(message));
            File imageFile = new File(imagePath);
            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
                outputStream.write(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save current image!");
            }
            MqttMessage mqttMessage = new MqttMessage();
            String test= String.valueOf(webFaceCompare.similarity(imagePath,filePath));
            mqttMessage.setPayload(test.getBytes(StandardCharsets.UTF_8));
            mqttClient.publish("zhy",mqttMessage);
        });
    }

    public void subscribeImgFromAPP(String topic) throws MqttException {
        mqttClient.subscribe(topic, (topic7, message) -> {
            byte[] imageByte = Base64.decodeBase64(String.valueOf(message));
            File imageFile = new File(filePath );
            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
                outputStream.write(imageByte);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save image!");
            }
            System.out.println("save image successfully");
        });
    }
    public void subscribeAudio(String topic) throws MqttException {
        mqttClient.subscribe(topic, (topic3, message) -> {
            String AudioPath="C:\\Users\\Lenovo\\Music\\Audio\\CurrentAudio.mp3";
                    try {
                        // 解码Base64音频数据
                        byte[] audioData = Base64.decodeBase64(String.valueOf(message));
                        // 将音频数据写入MP3文件
                        FileOutputStream fos = new FileOutputStream(AudioPath);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        bos.write(audioData);
                        bos.flush();
                        bos.close();
                        System.out.println("音频已保存为MP3文件：" + AudioPath);
                    } catch (Exception e) {
                        System.out.println("保存音频为MP3文件时发生错误：" + e.getMessage());
                    }
            VoiceprintRecognition voiceprintRecognition=new VoiceprintRecognition();

            MqttMessage mqttMessage = new MqttMessage();
            String test= String.valueOf(voiceprintRecognition.VoicePrintCompare(AudioPath));
            mqttMessage.setPayload(test.getBytes(StandardCharsets.UTF_8));
            mqttClient.publish("audio",mqttMessage);
        });
    }
    private Convert audioConversionService;
    public void AcceptMP3(String topic)throws MqttException{
        mqttClient.subscribe(topic,(topic4,waveMessage)->{
           byte  waveAudio[]=waveMessage.getPayload();
            try {
                // 将音频数据写入MP3文件
                FileOutputStream fos = new FileOutputStream(audioPathWave);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(waveAudio);
                bos.flush();
                bos.close();
                audioConversionService.convertWavToMp3(audioPathWave, audioPathMp3);
                System.out.println("音频已保存为MP3文件：" + audioPathMp3);
            } catch (Exception e) {
                System.out.println("保存音频为MP3文件时发生错误：" + e.getMessage());
            }
            VoiceprintRecognition voiceprintRecognition = new VoiceprintRecognition();
            voiceprintRecognition.VoicePrintCompare(audioPathMp3);
        });
    }
    String audioPathWave2 = "C:\\Users\\Lenovo\\Music\\Audio\\UserAudio.wave";
    String audioPathMp32 = "C:\\Users\\Lenovo\\Music\\Audio\\UserAudio.mp3";
    public void AcceptFormAPP(String topic)throws MqttException{
        mqttClient.subscribe(topic,(topic4,waveMessage)->{
            Convert convert=new Convert();
            byte  waveAudio[]=waveMessage.getPayload();
            try {
                // 将音频数据写入MP3文件
                FileOutputStream fos = new FileOutputStream(audioPathWave2);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(waveAudio);
                bos.flush();
                bos.close();
                convert.convertWavToMp3(audioPathWave2, audioPathMp32);
                System.out.println("音频已保存为MP3文件：" + audioPathMp32);
            } catch (Exception e) {
                System.out.println("保存音频为MP3文件时发生错误：" + e.getMessage());
            }
            VoiceprintRecognition.SaveVoice(audioPathMp32);
        });
    }

}
*/