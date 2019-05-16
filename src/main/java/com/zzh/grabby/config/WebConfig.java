package com.zzh.grabby.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.zzh.grabby.exception.GrabbyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author zzh
 * @date 2019/1/11
 */
@Configuration
public class WebConfig {

    private TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

    private ZoneOffset zoneOffset = ZoneOffset.of("+8");

    /**
     * jackson2 json序列化 null字段输出为空串
     *
     * @return
     * @author zzh
     */
    @Bean
    @Primary
    //@ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        });

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //序列化日期格式
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());

        //反序列化日期格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objectMapper.registerModule(javaTimeModule);

        objectMapper.setTimeZone(timeZone);
        return objectMapper;
    }

    /**
     * LocalDateTime序列化
     */
    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(value.format(DATETIME_FORMATTER));
            gen.writeNumber(value.toInstant(zoneOffset).toEpochMilli());
        }
    }

    /**
     * LocalDateTime反序列化
     */
    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.ofEpochSecond(p.getValueAsLong() / 1000, 0, zoneOffset);
        }
    }

    /**
     * 接收前端入参日期的转换处理
     *
     * @return
     */
    @Bean
    public Converter<String, Date> DateConvert() {

        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                Date date = null;
                if (StringUtils.isNotBlank(source)) {
                    try {
                        date = new Date(Long.valueOf(source));
                    } catch (Exception e) {
                        throw new GrabbyException("日期格式有误");
                    }
                }
                return date;
            }
        };
    }

    /**
     * 接收前端入参日期的转换处理
     *
     * @return
     */
    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {

        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                LocalDateTime localDateTime = null;
                if (StringUtils.isNotBlank(source)) {
                    try {
                        Instant instant = Instant.ofEpochMilli(Long.valueOf(source));
                        ZoneId zone = ZoneId.systemDefault();
                        localDateTime = LocalDateTime.ofInstant(instant, zone);
                    } catch (Exception e) {
                        throw new GrabbyException("日期格式有误");
                    }
                }
                return localDateTime;
            }
        };

    }
}
