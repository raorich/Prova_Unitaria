package domain;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    private JourneyService journeyService;
    private UserAccount userAccount;
    private VehicleID vehicleID;
    private StationID stationID;
    private GeographicPoint location;

    @BeforeEach
    void setUp() {
        // Crear los objetos necesarios antes de cada prueba
        journeyService = new JourneyService();
        userAccount = new UserAccount("user123");
        vehicleID = new VehicleID("veh123");
        stationID = new StationID("station001");
        location = new GeographicPoint(40.7128f, -74.0060f); // Coordenadas de Nueva York
    }

    @Test
    void testSetServiceInit() {
        // Inicializar el trayecto
        LocalDateTime initTime = LocalDateTime.now();
        journeyService.setServiceInit(initTime);

        // Verificar que el trayecto se haya iniciado correctamente
        assertTrue(journeyService.isInProgress(), "El trayecto debería estar en progreso.");
        assertEquals(initTime, journeyService.getInitDate(), "La fecha de inicio no es correcta.");
    }

    @Test
    void testSetServiceFinish() {
        // Finalizar el trayecto
        LocalDateTime endTime = LocalDateTime.now();
        journeyService.setServiceInit(LocalDateTime.now()); // Inicializar primero
        journeyService.setServiceFinish(endTime, 15.0f, 10, 30.0f, new BigDecimal("10.00"));

        // Verificar que el trayecto se haya finalizado correctamente
        assertFalse(journeyService.isInProgress(), "El trayecto debería haber terminado.");
        assertEquals(endTime, journeyService.getEndDate(), "La fecha de fin no es correcta.");
        assertEquals(15.0f, journeyService.getDistance(), "La distancia no es correcta.");
        assertEquals(10, journeyService.getDuration(), "La duración no es correcta.");
        assertEquals(30.0f, journeyService.getAvgSpeed(), "La velocidad promedio no es correcta.");
        assertEquals(new BigDecimal("10.00"), journeyService.getImportAmount(), "El importe no es correcto.");
    }

    @Test
    void testSetOrgStatID() {
        // Establecer la estación de origen
        journeyService.setOrgStatID(stationID);

        // Verificar que el ID de la estación se ha establecido correctamente
        assertEquals(stationID, journeyService.getOrgStatID(), "El ID de la estación de origen no es correcto.");
    }

    @Test
    void testSetOriginPoint() {
        // Establecer la ubicación del conductor
        journeyService.setOriginPoint(location);

        // Verificar que la ubicación se ha establecido correctamente
        assertEquals(location, journeyService.getOriginPoint(), "La ubicación del conductor no es correcta.");
    }

    @Test
    void testSetUserAccount() {
        // Establecer la cuenta de usuario
        journeyService.setUserAccount(userAccount);

        // Verificar que la cuenta de usuario se ha establecido correctamente
        assertEquals(userAccount, journeyService.getUserAccount(), "La cuenta de usuario no es correcta.");
    }

    @Test
    void testSetVehicleID() {
        // Establecer el ID del vehículo
        journeyService.setVehicleID(vehicleID);

        // Verificar que el ID del vehículo se ha establecido correctamente
        assertEquals(vehicleID, journeyService.getVehicleID(), "El ID del vehículo no es correcto.");
    }
}
