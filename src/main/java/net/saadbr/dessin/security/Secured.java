package net.saadbr.dessin.security;

import java.lang.annotation.*;

/**
 * @author saade
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Secured {
    Role[] value();
}
