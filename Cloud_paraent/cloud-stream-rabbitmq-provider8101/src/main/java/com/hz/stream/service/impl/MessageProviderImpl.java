package com.hz.stream.service.impl;

import com.hz.stream.service.MessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import javax.annotation.Resource;
import java.util.UUID;

@EnableBinding(Source.class) // 将信道channel和exchange绑定在一起, Source定义消息的推送管到(生产者->Source)
public class MessageProviderImpl implements MessageProvider {

    @Resource(name = "output")
    private MessageChannel messageChannel;

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        // 消息生产者服务构建(build)一个Message消息对象，然后传递给Source池，再通过管道binding输出给消息中间件MQ
        // 最后消费者服务的Sink池中可以获取到Message对象
        messageChannel.send(MessageBuilder.withPayload(serial).build());
        System.out.println("*****send message, serial: " + serial);
        return null;
    }
}
