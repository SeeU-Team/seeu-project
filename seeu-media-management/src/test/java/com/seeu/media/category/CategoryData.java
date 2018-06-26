package com.seeu.media.category;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Retention(RUNTIME)
@Target(TYPE)

@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:category-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:category-cleanup.sql")
public @interface CategoryData {
}