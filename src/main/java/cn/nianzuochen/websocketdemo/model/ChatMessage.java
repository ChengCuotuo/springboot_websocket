package cn.nianzuochen.websocketdemo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者
     */
    private String sender;

    public enum MessageType {
        CHAT,          // 消息
        JOIN,           // 加入
        LEAVE          // 离开
    }
}
