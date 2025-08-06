package de.ostfale.qk.annotation;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuditLog {
    @Nonbinding // This allows us to provide a default value or use it without specifying
    String value() default "Default Audit"; // Optional: description of the audited action
}
