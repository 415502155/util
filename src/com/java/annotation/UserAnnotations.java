package com.java.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface UserAnnotations {

	UserAnnotation[] value();
}
