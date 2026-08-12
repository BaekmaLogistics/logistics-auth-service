package com.sparta.logistics;

import com.sparta.logistics.infrastructure.feign.config.OpenFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;


@ConfigurationPropertiesScan
@EnableFeignClients(
        defaultConfiguration = OpenFeignConfig.class
)
@SpringBootApplication
public class LogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
    }

}
