package de.ostfale.qk.ageverifier;

import de.ostfale.qk.StreamingService;
import de.ostfale.qk.User;
import de.ostfale.qk.interceptor.AuditLogInterceptor;
import de.ostfale.qk.exception.ContentRestrictionException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AgeVerificationInterceptorTest {

    @Inject
    StreamingService streamingService;

    @BeforeEach
    void setUp() {
        AuditLogInterceptor.lastLoggedMessage_test_only = null; // Reset audit log for consistent testing
    }

    @Test
    void testMatureContentAccessGrantedForAdult() {

        User adultUser = new User("AdultAdam", 25, false);
        String movieId = "movie_for_adults";

        String result = null;
        try {
            result = streamingService.streamMatureContent(adultUser, movieId);
        } catch (ContentRestrictionException e) {
            fail("Adult user should not be restricted: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(String.format("User %s is streaming mature content: %s.", adultUser.getUsername(), movieId), result);
        assertNotNull(AuditLogInterceptor.lastLoggedMessage_test_only, "Audit log should be present for successful access.");
        assertTrue(AuditLogInterceptor.lastLoggedMessage_test_only.contains("Mature Content Playback"));
    }

    @Test
    void testMatureContentAccessDeniedForMinor() {
        User minorUser = new User("MinorMary", 15, false);
        String movieId = "movie_for_adults";

        ContentRestrictionException thrown = assertThrows(ContentRestrictionException.class, () -> streamingService.streamMatureContent(minorUser, movieId));

        assertTrue(thrown.getMessage().contains("does not meet minimum age requirement of 18"));
        assertNull(AuditLogInterceptor.lastLoggedMessage_test_only,
                "AuditLogInterceptor should not have logged method entry if AgeVerificationInterceptor blocked execution.");
    }
}
