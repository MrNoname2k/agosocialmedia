package org.api.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.util.Date;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy/MM/dd")
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}