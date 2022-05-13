package com.hz.stream.controller;

import com.hz.stream.service.MessageProvider;
import com.hz.stream.service.impl.MessageProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @Autowired
    private MessageProviderImpl messageProvider;

    @GetMapping("/sendMsg")
    public String sendMessage(){

      return messageProvider.send();
    }
}
