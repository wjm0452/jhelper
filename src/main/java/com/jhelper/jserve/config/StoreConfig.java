package com.jhelper.jserve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jhelper.store.Store;

@Configuration
public class StoreConfig {

    @Bean
    public Store store() {
        return new Store();
    }
}
