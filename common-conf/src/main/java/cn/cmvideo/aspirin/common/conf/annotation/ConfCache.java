package cn.cmvideo.aspirin.common.conf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.RUNTIME)   
@Target (ElementType.FIELD) 
public @interface ConfCache {

	String key();
	String default_value() default "";
	
}
