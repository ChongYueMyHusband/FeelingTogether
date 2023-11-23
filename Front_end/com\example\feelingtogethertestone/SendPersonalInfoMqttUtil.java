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


// 通过 MQTT 协议发送账号信息的工具类，它使用 Eclipse Paho MQTT 客户端库来建立 MQTT 连接并发送消息


class SendPersonalInfoMqttUtil {
    private String personalInfoString;
    private Context context;

    private final String TAG = "SendPersonalInfoMqttUtil";
    final private String PUB_TOPIC = "pub_personalInfo";	//发送主题
    final private String SUB_TOPIC = "sub_personalInfo";	//订阅主题

    /* 阿里云Mqtt服务器域名 */
//    final String host = "ws://emqx@114.55.245.48:8083";	//服务器地址（协议+地址+端口号）

    final String host = "ws://47.113.150.172:8083";
    private String clientId = "mqttx_7u0b459v";	//客户端ID
    private String userName = "MuM";	//用户名
    private String passWord = "123456";	//密码
    public String arrivedResult;

    MqttAndroidClient mqttAndroidClient;


    public SendPersonalInfoMqttUtil(String personalInfoString, Context context){
        this.personalInfoString = personalInfoString;
        this.context = context;
    }

    //执行 MQTT 连接和消息发布的核心方法
    protected void sendPersonalInfo() {
        /* 创建MqttConnectOptions对象并配置username和password */
        // 用户名和密码是用来进行 MQTT 服务器的身份验证的。这些凭证必须与您的 MQTT 服务器的配置相匹配
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(passWord.toCharArray());

        /* 创建MqttAndroidClient对象, 并设置回调接口 */
        // MQTT 客户端 - 这个客户端会与 MQTT 服务器建立连接。
        //host：表示 MQTT 服务器的主机地址，即要连接的服务器的地址。
        //clientId：表示客户端的唯一标识符，用于在 MQTT 服务器上标识客户端。
        mqttAndroidClient = new MqttAndroidClient(context, host, clientId);

        // MQTT 客户端的回调接口
        // 它包含了一些回调方法，用于处理连接状态和消息的处理
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            // 当与 MQTT 服务器的连接丢失时调用
            public void connectionLost(Throwable cause) {   //当与 MQTT 服务器的连接丢失时调用
                Log.i(TAG, "connection lost");
            }

            @Override
            // 当从 MQTT 服务器接收到新消息时调用
            // 一旦成功订阅了某个 MQTT 主题，并在客户端设置了 messageArrived 方法来处理接收到的消息，
            // 每当服务器通过该主题发送消息时，都会自动触发 messageArrived 方法来接收和处理这些消息。
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.i(TAG, "topic: " + topic + ", msg: " + new String(message.getPayload()));
                // 在这里判断接收到的消息，根据返回值进行下一步操作
                String receivedMessage = new String(message.getPayload());
                Log.i(TAG, "topic: " + topic + ", msg: " + receivedMessage);

                // 根据服务器返回的1、0来判断是否登陆
                // 待补：设置一个private变量来控制
                if (receivedMessage.equals("1")) {
                    // 如果服务器返回 1，
                    // 在这里执行相应的操作，如跳转到下一个界面
                } else if (receivedMessage.equals("0")) {
                    // 如果服务器返回 0，
                    // 在这里执行相应的操作，如显示错误消息
                }
            }

            @Override
            // 当成功传递消息到 MQTT 服务器时调用
            public void deliveryComplete(IMqttDeliveryToken token) {    //当成功传递消息到 MQTT 服务器时调用
                Log.i(TAG, "msg delivered");
            }
        });

        /* Mqtt建连 */
        //整个建连过程是异步的，connect方法会立即返回，实际连接的结果会通过回调函数通知。
        try {
            mqttAndroidClient.connect(mqttConnectOptions,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // 当连接成功建立时，会打印日志信息表示连接成功，并调用 subscribeTopic 方法来订阅指定的主题
                    Log.i(TAG, "connect succeed");
                    subscribeTopic(SUB_TOPIC);
                    publishMessage(personalInfoString);
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
     * 订阅特定的主题 - 将 MQTT 客户端连接到指定的主题，以便接收该主题下的消息, 允许客户端接收特定主题上发布的消息
     * 一旦成功订阅了某个 MQTT 主题，并在客户端设置了 messageArrived 方法来处理接收到的消息，每当服务器通过该主题发送消息时，都会自动触发 messageArrived 方法来接收和处理这些消息。
     * @param topic mqtt主题
     */
    public void subscribeTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
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
     * @param personalInfoString 消息载荷
     */
    public void publishMessage(String personalInfoString) {
        try {
            // 检查 MQTT 客户端 (mqttAndroidClient) 是否已连接。如果未连接，它尝试通过 mqttAndroidClient.connect() 连接到 MQTT 服务器
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            // 创建一个 MqttMessage 对象 message，用于封装要发布的消息。
            MqttMessage message = new MqttMessage();
            // 消息内容是通过将 personalInfoString 字符串转换为字节数组来设置的。
            message.setPayload(personalInfoString.getBytes());
            // 设置消息的服务质量（Quality of Service，QoS）为 0，这表示消息将以“最多一次”传递，不保证消息的可靠性
            message.setQos(0);
            // 使用 mqttAndroidClient.publish() 方法将消息发布到指定的 MQTT 主题 PUB_TOPIC，
            // 并提供了一个回调接口 IMqttActionListener 来处理发布操作的结果。
            // 如果发布成功，会调用 onSuccess 方法，否则会调用 onFailure 方法
            mqttAndroidClient.publish(PUB_TOPIC, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!");
                    Log.i("TestAccount", personalInfoString);
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


    public void processReceivedData(String payload){
        arrivedResult = payload;
    }

}



