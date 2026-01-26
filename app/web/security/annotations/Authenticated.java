package web.security.annotations;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(AuthenticationAction.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {

    AuthSource[] values() default { AuthSource.BEARER };

}
