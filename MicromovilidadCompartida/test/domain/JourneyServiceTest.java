package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

class JourneyServiceTest {
    @Test
    public void testSetServiceInit() {
        JourneyService journeyService = new JourneyService();
        LocalDateTime now = LocalDateTime.now();

        journeyService.setServiceInit(now);
        assertNotNull(journeyService.getInitDate());
        assertTrue(journeyService.isInProgress());
    }

    @Test
    public void testSetServiceFinish() {
        JourneyService journeyService = new JourneyService();
        LocalDateTime start = LocalDateTime.now();
        journeyService.setServiceInit(start);

        LocalDateTime end = LocalDateTime.now().plusHours(1);
        journeyService.setServiceFinish(end, 10.5f, 60, 10.5f, new BigDecimal("15.50"));

        assertEquals(end, journeyService.getEndDate());
        assertFalse(journeyService.isInProgress());
    }

}