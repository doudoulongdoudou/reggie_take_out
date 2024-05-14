package com.ddl.reggie.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: Knife4jConfig
 * Package: com.ddl.reggie.config
 * Description:
 *
 * @Author 豆豆龙
 * @Create 2024/5/14 18:49
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 接口文档标题
                        .title("瑞吉外卖")
                        // 接口文档简介
                        .description("瑞吉外卖接口文档")
                        // 接口文档版本
                        .version("1.0")
                        // 开发者联系方式
                        .contact(new Contact()
                                .name("豆豆龙")
                                .email("feng0039@gmail.com"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("瑞吉外卖-接口文档")
                        .url("http://localhost:8080"));
    }
}
