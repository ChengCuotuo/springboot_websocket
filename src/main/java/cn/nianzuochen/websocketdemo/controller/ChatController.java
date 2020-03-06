package cn.nianzuochen.websocketdemo.controller;

import cn.nianzuochen.websocketdemo.model.ChatMessage;
import cn.nianzuochen.websocketdemo.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 当我们接收到用户发送消息的请求时，就把消息转发给 redis 的频道 websocket.msgToAll
     * @param chatMessage
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            LOGGER.info(msgToAll, chatMessage);
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 在处理消息之后发送消息
     * 使用 @MessageMapping 或者 @SubscribeMessage 注解可以处理客户端发送过来的消息，并且选择发给发是否有返回值
     *
     * 如果 @MessageMapping 注解的控制器方法有返回值的话，返回值会被发送到消息代理，只不过会添加上 "/topic"
     * 可以使用 @SendTo 重写消息的目的地
     *
     * @SubscribeMapping 注解的控制器方法有返回值的话，返回值会直接发送到客户端，不经过代理
     * 如果加上 @SendTo 注解的话，则呀经过消息代理
     *
     *
     *  在任意地方发送消息
     *  spring-websocket 定义了一个 SimpMessageSendingOperations 接口或者使用 SimpMessagingTemplate
     *  可以实现自由的向任意目的地发送消息，并且订阅此目的地的所有用户都能收到消息
     *
     * @param chatMessage
     * @param headerAccessor
     * @return
     */

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload  ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        LOGGER.info("username: " + chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
