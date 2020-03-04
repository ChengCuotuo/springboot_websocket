package cn.nianzuochen.websocketdemo.controller;

import cn.nianzuochen.websocketdemo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * 我们在 websocket 的配置中，从目的地以 /app 开头的客户端发送的所有信息都将路由到这些
 * 使用 @MessageMapping 注释的消息处理方法
 *
 * 例如：
 *      /app/chat.sendMessage 的消息都将路由到 sendMessage() 方法，并且具有
 *      /app/chat.addUser 的消息都将路由到 addUser() 方法
 */
@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload  ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
