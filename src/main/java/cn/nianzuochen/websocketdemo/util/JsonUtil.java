package cn.nianzuochen.websocketdemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json转换
 */
public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 将 java 对象转换成 json 字符串
     * @param object
     * @return
     */
    public static String parseObjToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }

        return json;
    }

    public static<T> T parseJsonToObj(String json, Class<T> c) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
