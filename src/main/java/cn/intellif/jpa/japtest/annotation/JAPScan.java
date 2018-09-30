package cn.intellif.jpa.japtest.annotation;

import cn.intellif.jpa.japtest.aware.InterfaceRegisterUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(InterfaceRegisterUtils.class)
public @interface JAPScan {
    String[] packages();
}
