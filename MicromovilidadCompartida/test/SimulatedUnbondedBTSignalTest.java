import exceptions.ConnectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.SimulatedUnbondedBTSignal;

import static org.junit.jupiter.api.Assertions.*;

class SimulatedUnbondedBTSignalTest {

    private SimulatedUnbondedBTSignal btSignalAvailable;
    private SimulatedUnbondedBTSignal btSignalUnavailable;

    @BeforeEach
    void setUp() {
        btSignalAvailable = new SimulatedUnbondedBTSignal(true);
        btSignalUnavailable = new SimulatedUnbondedBTSignal(false);
    }

    @Test
    void testBTbroadcast_WithAvailableBluetooth() {
        assertDoesNotThrow(() -> btSignalAvailable.BTbroadcast());
    }

    @Test
    void testBTbroadcast_WithUnavailableBluetooth() {
        ConnectException exception = assertThrows(ConnectException.class, () -> btSignalUnavailable.BTbroadcast());
        assertEquals("Error en la conexión Bluetooth.", exception.getMessage());// Verificamos que lanza la excepción ConnectException cuando Bluetooth no está disponible
    }
    @Test
    void testConstructor_WithValidBluetoothAvailability() {
        assertNotNull(btSignalAvailable);
        assertNotNull(btSignalUnavailable);
    }


}