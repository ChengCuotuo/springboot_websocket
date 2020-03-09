package cn.nianzuochen.websocketdemo.service;

import cn.nianzuochen.websocketdemo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 发送消息
     * 需要在监听到消息调用后，调用这个 service ，在 redis 监听消息处理专用类中
     * @param chatMessage
     */
    public void sendMsg(@Payload ChatMessage chatMessage) {
        LOGGER.info("Send msg by simpMessageSendingOperations: " + chatMessage);
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }

    public void alertUserStatus(@Payload ChatMessage chatMessage) {
        LOGGER.info("Alert user online by simpMessageSendingOperations： " + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }
}
