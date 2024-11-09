package org.cris6h16.Controllers.Advices.Properties.others;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public  @interface ValidatePropertiesNotNullOrEmpty {
    Class<? extends RuntimeException> exception() default IllegalStateException.class;
    String message() default "Property cannot be null or empty, property name";
}


@Component
 class NotNullOrEmptyValidatorPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        ValidatePropertiesNotNullOrEmpty annotation = clazz.getAnnotation(ValidatePropertiesNotNullOrEmpty.class);

        if (annotation != null) {
            Class<? extends RuntimeException> exceptionClass = annotation.exception();
            String messagePrefix = annotation.message();

            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(bean);

                    if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                        String message = messagePrefix + ": " + field.getName();
                        throw exceptionClass.getConstructor(String.class).newInstance(message);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error processing field: " + field.getName(), e);
                }
            }
        }
        return bean;
    }
}