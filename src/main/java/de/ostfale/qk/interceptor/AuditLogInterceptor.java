package de.ostfale.qk.interceptor;

import de.ostfale.qk.annotation.AuditLog;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Arrays;

import static io.quarkus.arc.ComponentsProvider.LOG;

/**
 * CDI interceptors allow you to modularize cross-cutting concerns like logging, metrics, and security.
 * The @AuditLog interceptor will capture method calls for user actions like streaming or accessing account details.
 */

@AuditLog
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 200) // Define a priority
public class AuditLogInterceptor {

    // For testing purposes, we'll store the last log message
    // In a real app, you'd use a robust logging framework or send to a log aggregator
    public static String lastLoggedMessage_test_only = null;

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        String className = context.getTarget().getClass().getSimpleName();
        String params = Arrays.toString(context.getParameters());

        // Get the optional value from the annotation if needed
        AuditLog auditLogAnnotation = context.getMethod().getAnnotation(AuditLog.class);
        if (auditLogAnnotation == null) { // Could be on class level
            auditLogAnnotation = context.getTarget().getClass().getAnnotation(AuditLog.class);
        }
        String auditDescription = auditLogAnnotation != null ? auditLogAnnotation.value() : "N/A";

        String logMessage = String.format("AUDIT [%s]: Method '%s.%s' called with params %s.", auditDescription, className, methodName, params);
        LOG.info(logMessage);
        lastLoggedMessage_test_only = logMessage; // Store for testing

        Object ret = context.proceed(); // Execute the original method

        LOG.info(String.format("AUDIT [%s]: Method '%s.%s' completed.", auditDescription, className, methodName));
        return ret;
    }
}
