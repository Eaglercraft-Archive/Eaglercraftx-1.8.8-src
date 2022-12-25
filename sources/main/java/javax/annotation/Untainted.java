package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

/**
 * This annotation is used to denote String values that are untainted, i.e.
 * properly validated.
 * <p>
 * For example, this annotation should be used on the String value which
 * represents SQL query to be passed to database engine.
 * <p>
 * When this annotation is applied to a method it applies to the method return
 * value.
 *
 * @see Tainted
 */
@Documented
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Untainted {
	When when() default When.ALWAYS;
}
