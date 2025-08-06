package de.ostfale.qk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 *  Using @VipAccessRequired, we’ll block regular users from accessing early-release movies — unless the config flag is off.
 *  + Inject configuration into interceptors
 *  + Enforce feature flags at runtime
 */

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface VipAccessRequired {
}
