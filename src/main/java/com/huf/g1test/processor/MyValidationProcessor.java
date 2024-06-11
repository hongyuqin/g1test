package com.huf.g1test.processor;

import com.huf.g1test.annotation.MyValidation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyValidationProcessor {

    public static void validateParameter(Object parameter) {
        // 获取参数的注解信息
        MyValidation validation = parameter.getClass().getAnnotation(MyValidation.class);
        
        if (validation != null) {
            int length = validation.length();
            int parameterLength = parameter.toString().length();
            log.info("validateParameter param : {}", parameter);
            if (parameterLength > length) {
                throw new RuntimeException("too long");
            }
            // 根据注解的属性进行校验逻辑
            // 可以根据需求自定义校验逻辑，例如判断参数是否为空、长度是否符合要求等
        }
    }
}