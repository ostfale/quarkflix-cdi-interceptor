package de.ostfale.qk.vipaccess;

import de.ostfale.qk.StreamingService;
import de.ostfale.qk.User;
import de.ostfale.qk.exception.ContentRestrictionException;
import de.ostfale.qk.interceptor.AuditLogInterceptor;
import de.ostfale.qk.exception.FeatureAccessException;
import de.ostfale.qk.interceptor.PerformanceTrackingInterceptor;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class VipAccessInterceptorTest {

    @Inject
    StreamingService streamingService;

    @BeforeEach
    void setup() {
        AuditLogInterceptor.lastLoggedMessage_test_only = null;
        PerformanceTrackingInterceptor.lastExecutionTime_test_only = -1;
    }

    @Test
    void testVipUserAccessesPremiere() {
        User vipUser = new User("VipVictor", 25, true);
        String result = streamingService.playPremiereMovie(vipUser, "ExclusiveFilm");
        assertNotNull(result);
        assertTrue(result.contains("VIP User VipVictor is playing premiere movie"));
        assertNotNull(AuditLogInterceptor.lastLoggedMessage_test_only, "Audit log should exist for VIP user.");
        assertTrue(PerformanceTrackingInterceptor.lastExecutionTime_test_only >= 0,
                "Performance should be tracked for VIP user.");
    }

    @Test
    void testNonVipUserBlockedFromPremiere() {
        User nonVipUser = new User("RegularRita", 25, false);
        FeatureAccessException thrown = assertThrows(FeatureAccessException.class, () -> {
            streamingService.playPremiereMovie(nonVipUser, "ExclusiveFilm");
        });
        assertTrue(thrown.getMessage().contains("is not a VIP"));
        assertNull(AuditLogInterceptor.lastLoggedMessage_test_only,
                "AuditLogInterceptor should not log if blocked by VIP check.");
    }

    @Test
    void testVipUserButUnderAgeBlockedFromPremiere() {
        User youngVipUser = new User("YoungVipYara", 12, true); // VIP but underage (premiere requires 13)
        assertThrows(ContentRestrictionException.class, () -> { // Expecting age restriction
            streamingService.playPremiereMovie(youngVipUser, "ExclusiveFilmPG13");
        });
        // VipAccessInterceptor (priority 50) passes.
        // AgeVerificationInterceptor (priority 100) should block.
        // Performance (150) and Audit (200) should not be fully executed for the method
        // itself.
        assertNull(AuditLogInterceptor.lastLoggedMessage_test_only,
                "AuditLogInterceptor should not log method success if blocked by age check.");
    }
}
