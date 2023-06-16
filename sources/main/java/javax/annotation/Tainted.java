package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

/**
 * This annotation is used to denote String values that are tainted, i.e. may
 * come from untrusted sources without proper validation.
 * <p>
 * For example, this annotation should be used on the String value which
 * represents raw input received from the web form.
 * <p>
 * When this annotation is applied to a method it applies to the method return
 * value.
 *
 * @see Untainted
 */
@Documented
@TypeQualifierNickname
@Untainted(when = When.MAYBE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tainted {

}
