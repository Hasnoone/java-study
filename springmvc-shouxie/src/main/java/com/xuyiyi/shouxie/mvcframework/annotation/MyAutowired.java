package com.xuyiyi.shouxie.mvcframework.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {
    String value() default "";
}
