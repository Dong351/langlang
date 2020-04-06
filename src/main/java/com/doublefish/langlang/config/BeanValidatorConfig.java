package com.doublefish.langlang.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class BeanValidatorConfig extends WebMvcConfigurerAdapter {
    private MessageSource messageSource;

    public BeanValidatorConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public Validator getValidator() {
        return super.getValidator();
    }

    @Bean
    @Primary
    public Validator validator(){
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }

}
