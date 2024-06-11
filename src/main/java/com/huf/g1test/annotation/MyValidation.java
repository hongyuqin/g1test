package com.huf.g1test.annotation;

import org.springframework.validation.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 仅适用于方法参数
@Retention(RetentionPolicy.RUNTIME) // 在运行时保留注解信息
public @interface MyValidation {
    int length() default -1;
    //Class<? extends Validator> validator() default DefaultValidator.class;

    // 定义注解的属性
    // 可以根据需求定义多个属性，用于参数校验的不同条件
}