package com.rontseng.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface QueryColumnMapping {
	String mappedEntityProperty() default "";

	boolean ignore() default false;
}
