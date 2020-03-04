package cn.nianzuochen.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// 用来启动我们的 WebSocket 服务器
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册一个 websocket 端点（ws），客户端将使用它连接到我们的 websocket 服务器
     * withSocketJS() 是用来支持 websocket 的浏览器启用后备选项，使用了 SocketJS
     *
     * STOMP 是来自 Spring 框架 STOMP 实现。STOMP 代表简单文本导向的消息传递协议。
     * 是一种消息传递协议，用户定义数据交换的格式和规则。
     * 因为 websocket 只是一种通信协议，没有定义诸如一下内容：如何仅向订阅特定主题的用户
     * 发送消息，或者如何向特定用户发送消息。这个就需要使用 STOMP 来实现。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * 这个方法种配置了一个消息代理，用于将消息从一个客户端路由到另一个客户端
     * 第一行定义了以 "/app" 开头的信息应该路由到消息处理方法 （之后会定义这个方法）
     * 第二行定义了以 "/topic"  开头的消息应该路由到消息代理。消息代理向订阅特定主题的所有连接客户端广播消息。
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

}
