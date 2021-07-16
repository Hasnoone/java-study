package com.xuyiyi.shouxie.mvcframework.annotation;


import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MySecurity {

    String[] value() default "";

}
