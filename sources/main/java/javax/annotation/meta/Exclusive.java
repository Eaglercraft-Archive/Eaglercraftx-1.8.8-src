package javax.annotation.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be applied to the value() element of an annotation that
 * is annotated as a TypeQualifier.
 * 
 * <p>
 * For example, the following defines a type qualifier such that if you know a
 * value is {@literal @Foo(1)}, then the value cannot be {@literal @Foo(2)} or
 * {{@literal @Foo(3)}.
 * 
 * <pre>
 * &#064;TypeQualifier
 * &#064;interface Foo {
 * 	&#064;Exclusive
 * 	int value();
 * }
 * </pre>
 * 
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclusive {

}
