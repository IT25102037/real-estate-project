package com.realestate.project.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //Used to configure project settings
public class Config {
    @Bean  //Create and manage object automatically
    public ModelMapper getMapper(){
        return new ModelMapper();
    }
}

//Model mapper annotation used to convert dto to entities and entities an object