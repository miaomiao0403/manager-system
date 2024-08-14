package com.ebay.test.managersystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Configuration
@Component
public class LocalDBConfig {

    @Value("${datafile.path}")
    private String dataFilePath;


}
