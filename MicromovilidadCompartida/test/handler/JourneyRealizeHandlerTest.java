package handler;

import exceptions.ConnectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JourneyRealizeHandlerTest {
    @Test
    public void testBroadcastStationID() throws ConnectException {
        JourneyRealizeHandler handler = new JourneyRealizeHandler();
        String statID = "Station123";

        assertEquals("Station123", handler.broadcastStationID(statID));
    }

    @Test
    public void testBroadcastStationID_Exception() {
        JourneyRealizeHandler handler = new JourneyRealizeHandler();

        Exception exception = assertThrows(ConnectException.class, () -> {
            handler.broadcastStationID("");
        });

        assertEquals("No se puede recibir la señal de la estación.", exception.getMessage());
    }

}