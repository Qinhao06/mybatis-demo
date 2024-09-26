package org.example.build;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    String value();

}
