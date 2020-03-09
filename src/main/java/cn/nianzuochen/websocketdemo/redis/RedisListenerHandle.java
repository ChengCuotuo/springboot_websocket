package cn.nianzuochen.websocketdemo.redis;

import cn.nianzuochen.websocketdemo.model.ChatMessage;
import cn.nianzuochen.websocketdemo.service.ChatService;
import cn.nianzuochen.websocketdemo.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * Redis 订阅频道处理类
 * MessageListenerAdapter 是 spring 整合 redis 的消息监听器
 */
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerHandle.class);

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatService chatService;

    /**
     * 收到监听消息
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // redisTemplate.convertAndSend 消息存储再 redis 的时候，首先使用的 Json 字符串，然后使用的 redisTemplete 进行
        // 了默认的编码，现在也需要进行转换
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String rawMsg;
        String  topic;

        try {
            // 反序列化，将 byte[] 转成 string
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            LOGGER.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }

        if (msgToAll.equals(topic)) {
            LOGGER.info("Send message to all users:" + rawMsg);
            ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
            // 发送信息给所有在线用户
            chatService.sendMsg(chatMessage);
        } else {
            LOGGER.warn("No further operation with this topic!");
        }
    }
}
