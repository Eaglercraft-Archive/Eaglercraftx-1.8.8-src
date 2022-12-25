package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation is applied to a method, it indicates that if this method
 * is overridden in a subclass, the overriding method should invoke this method
 * (through method invocation on super).
 * <p>
 * An example of such method is {@link Object#finalize()}.
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OverridingMethodsMustInvokeSuper {

}
