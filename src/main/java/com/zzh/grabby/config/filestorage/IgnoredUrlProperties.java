package com.zzh.grabby.config.filestorage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zzh
 * @date 2019/1/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "grabby.ignored")
public class IgnoredUrlProperties {
    private List<String> urls;
}
