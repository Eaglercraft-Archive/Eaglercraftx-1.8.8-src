package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to annotate a method parameter to indicate that this method will close
 * the resource.
 *
 * @see WillCloseWhenClosed
 * @see WillNotClose
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WillClose {

}
