package myspring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(MyComponentsScan.class)
public @interface MyComponentScan {
    String[] path();

    Class<? extends Annotation>[] supportedComponents();
}
