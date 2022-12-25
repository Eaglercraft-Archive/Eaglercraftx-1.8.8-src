package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.meta.When;

/**
 * This annotation is used to denote a method whose return value should always
 * be checked after invoking the method.
 */
@Documented
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReturnValue {
	When when() default When.ALWAYS;
}
