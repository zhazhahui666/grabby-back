package com.zzh.grabby.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzh
 * @date 2018/10/15
 */
@Configuration
@MapperScan({"com.zzh.grabby.mapper","com.zzh.grabby.activiti.mapper"})
public class MybatisplusConfig {


    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
