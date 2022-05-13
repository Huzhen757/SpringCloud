package com.hz.stream.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class ReceiveMessageController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        // 发送的消息是String接受的message也必须是String
        // 发送消息使用withPayload方法，接受消息必须是getPayload()
        System.out.println("consumer 2, ----> receive: " + message.getPayload()+"\t server port "+ serverPort);
    }

}
