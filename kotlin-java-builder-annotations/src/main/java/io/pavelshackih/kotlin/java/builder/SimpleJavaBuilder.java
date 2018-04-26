package io.pavelshackih.kotlin.java.builder;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Annotations that indicates generate classic java
 * builders for kotlin data classes.
 */
@Retention(CLASS)
@Target(TYPE)
public @interface SimpleJavaBuilder {
}
