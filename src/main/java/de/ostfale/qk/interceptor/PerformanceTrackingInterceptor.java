package de.ostfale.qk.interceptor;

import de.ostfale.qk.annotation.TrackPerformance;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import static io.quarkus.arc.ComponentsProvider.LOG;

/**
 * Order of Execution (based on priority):
 *  + AgeVerificationInterceptor (if applied, Priority 100 - most "outer")
 *  + PerformanceTrackingInterceptor (Priority 150)
 *  + AuditLogInterceptor (Priority 200 - most "inner" before method)
 *  + StreamingService.getContinueWatchingList() method execution.
 *
 *  The logs should reflect this. For getContinueWatchingList, only Performance and Audit will apply.
 *  Performance (150) will wrap Audit (200)
 */

@TrackPerformance
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 150) // Between AgeVerifier (100) and AuditLog (200)
public class PerformanceTrackingInterceptor {

    public static long lastExecutionTime_test_only = -1;

    @AroundInvoke
    Object track(InvocationContext context) throws Exception {
        long startTime = System.nanoTime();
        try {
            return context.proceed();
        } finally {
            long endTime = System.nanoTime();
            long durationNanos = endTime - startTime;
            long durationMillis = durationNanos / 1_000_000;
            lastExecutionTime_test_only = durationMillis; // For testing

            LOG.infof("PERF_TRACK: Method %s.%s executed in %d ms (%d ns).",
                    context.getTarget().getClass().getSimpleName(),
                    context.getMethod().getName(),
                    durationMillis,
                    durationNanos);
        }
    }
}
