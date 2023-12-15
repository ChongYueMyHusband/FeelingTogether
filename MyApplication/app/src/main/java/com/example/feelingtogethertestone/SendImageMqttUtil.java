package com.example.feelingtogethertestone;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SendImageMqttUtil {
    private String imageString;
    private Context context;

    private final String TAG = "SendImageMqttUtil";
    final private String PUB_TOPIC = "pub_image";	//发送主题
    final private String SUB_TOPIC = "sub_image";	//订阅主题

    /* 阿里云Mqtt服务器域名 */
//    final String host = "ws://114.55.245.48:8083";	//服务器地址（协议+地址+端口号）
    final String host = "ws://emqx@47.113.150.172:8083";
    private String clientId = "mqttx_6u0b450a";	//客户端ID
    private String userName = "MuM";	//用户名
    private String passWord = "123456";	//密码

    MqttAndroidClient mqttAndroidClient;


    public SendImageMqttUtil(String imageString, Context context){
        this.imageString = imageString;
        this.context = context;
    }

    protected void send() {
        /* 创建MqttConnectOptions对象并配置username和password */
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(passWord.toCharArray());


        /* 创建MqttAndroidClient对象, 并设置回调接口 */
        //host：表示 MQTT 服务器的主机地址，即要连接的服务器的地址。
        //clientId：表示客户端的唯一标识符，用于在 MQTT 服务器上标识客户端。
        mqttAndroidClient = new MqttAndroidClient(context, host, clientId);

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {   //当与 MQTT 服务器的连接丢失时调用
                Log.i(TAG, "connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {    //当从 MQTT 服务器接收到新消息时调用
                Log.i(TAG, "topic: " + topic + ", msg: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {    //当成功传递消息到 MQTT 服务器时调用
                Log.i(TAG, "msg delivered");
            }
        });

        /* Mqtt建连 */
        //整个建连过程是异步的，connect 方法会立即返回，实际连接的结果会通过回调函数通知。
        try {
            mqttAndroidClient.connect(mqttConnectOptions,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // 当连接成功建立时，会打印日志信息表示连接成功，并调用 subscribeTopic 方法来订阅指定的主题
                    Log.i(TAG, "connect succeed");

                    subscribeTopic(SUB_TOPIC);
                    publishMessage(imageString);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //当连接建立失败时，会打印日志信息表示连接失败
                    Log.i(TAG, "connect failed1");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    /**
     * 订阅特定的主题
     * @param topic mqtt主题
     */
    public void subscribeTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
                    Log.i("TestImage", imageString);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "subscribed failed2");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向默认的主题/user/update发布消息
     * @param imageString 消息载荷
     */
    public void publishMessage(String imageString) {
        try {
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(imageString.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(PUB_TOPIC, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "publish failed!");
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

}



