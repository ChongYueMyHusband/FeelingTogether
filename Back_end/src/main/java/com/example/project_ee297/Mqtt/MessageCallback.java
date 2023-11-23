package com.example.project_ee297.Mqtt;

/**
 * consumer 消费者
 */
/*
@Component
@Slf4j
public class MessageCallback implements MqttCallback {

    @Autowired(required = false)
    private  MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        if (client == null || !client.isConnected()) {
            log.info("连接断开，正在重连....");
            try {
                client.connect();
                if(client.isConnected()){
                    log.info("mqtt重连完成.");
                }
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息主题 : " + topic);
        log.info("接收消息内容 : " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete---------" + token.isComplete());
    }
}
*/