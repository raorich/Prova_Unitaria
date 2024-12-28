package domain;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    private JourneyService journeyService;
    private ServiceID serviceID;
    private UserAccount userAccount;
    private VehicleID vehicleID;
    private GeographicPoint originPoint;
    private GeographicPoint endPoint;
    private StationID stationID;
    private BigDecimal importAmount;

    @BeforeEach
    void setUp() {
        // Inicializamos los objetos necesarios para la prueba
        ServiceID serviceID = new ServiceID("S1234");
        userAccount = new UserAccount("user123");
        vehicleID = new VehicleID("V1234");
        originPoint = new GeographicPoint(40, -74);  // Nueva York
        endPoint = new GeographicPoint(34, -118);    // Los Ángeles
        stationID = new StationID("ST123");
        importAmount = new BigDecimal("15.00");

        journeyService = new JourneyService(serviceID, userAccount, importAmount, 'C');
    }

    @Test
    void testConstructor_Success() {
        assertNotNull(journeyService);
        assertEquals(serviceID, journeyService.getServiceID());
        assertEquals(userAccount, journeyService.getUserAccount());
        assertEquals(importAmount, journeyService.getImportAmount());
        assertTrue(journeyService.isInProgress());  // El viaje debe estar en progreso por defecto
    }

    @Test
    void testSetServiceInit_Success() {
        LocalDateTime now = LocalDateTime.now();
        journeyService.setServiceInit(now);
        assertEquals(now, journeyService.getServiceInit());
    }

    @Test
    void testSetServiceInit_Failure_NullDate() {
        assertThrows(IllegalArgumentException.class, () -> journeyService.setServiceInit(null));
    }

    @Test
    void testSetServiceFinish_Success() {
        LocalDateTime now = LocalDateTime.now();
        journeyService.setServiceFinish(now.plusHours(1));
        assertEquals(now.plusHours(1), journeyService.getServiceFinish());
        assertFalse(journeyService.isInProgress());  // El trayecto debe haberse marcado como no en progreso
    }

    @Test
    void testSetServiceFinish_Failure_InvalidDate() {
        // Configurar el entorno
        LocalDateTime now = LocalDateTime.now();
        journeyService.setServiceInit(now);  // Establecer la fecha de inicio

        // Verificar que la fecha de inicio está correctamente configurada
        assertEquals(now, journeyService.getServiceInit());

        // Intentar establecer la fecha de fin con una fecha anterior a la de inicio
        LocalDateTime invalidFinishDate = now.minusHours(1);

        // Verificar que se lanza la excepción IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> journeyService.setServiceFinish(invalidFinishDate));
    }

    @Test
    void testSetDistance_Success() {
        journeyService.setDistance(1000.0f);  // 1 km
        assertEquals(1000.0f, journeyService.getDistance());
    }

    @Test
    void testSetDistance_Failure_NegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> journeyService.setDistance(-1.0f));
    }

    @Test
    void testSetDuration_Success() {
        journeyService.setDuration(30);  // 30 minutos
        assertEquals(30, journeyService.getDuration());
    }

    @Test
    void testSetDuration_Failure_NegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> journeyService.setDuration(-1));
    }

    @Test
    void testSetAvgSpeed_Success() {
        journeyService.setAvgSpeed(25.5f);  // 25.5 km/h
        assertEquals(25.5f, journeyService.getAvgSpeed());
    }

    @Test
    void testSetAvgSpeed_Failure_NegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> journeyService.setAvgSpeed(-5.0f));
    }

    @Test
    void testSetImportAmount_Success() {
        BigDecimal newAmount = new BigDecimal("20.00");
        journeyService.setImportAmount(newAmount);
        assertEquals(newAmount, journeyService.getImportAmount());
    }

    @Test
    void testSetImportAmount_Failure_NegativeValue() {
        BigDecimal negativeAmount = new BigDecimal("-5.00");
        assertThrows(IllegalArgumentException.class, () -> journeyService.setImportAmount(negativeAmount));
    }

    @Test
    void testSetOrgStatID() {
        journeyService.setOrgStatID(stationID);
        assertEquals(stationID, journeyService.getOrgStatID());
    }

    @Test
    void testSetOriginPoint() {
        journeyService.setOriginPoint(originPoint);
        assertEquals(originPoint, journeyService.getOriginPoint());
    }

    @Test
    void testSetEndPoint() {
        journeyService.setEndPoint(endPoint);
        assertEquals(endPoint, journeyService.getEndPoint());
    }

    @Test
    void testSetUserAccount() {
        journeyService.setUserAccount(userAccount);
        assertEquals(userAccount, journeyService.getUserAccount());
    }

    @Test
    void testSetVehicleID() {
        journeyService.setVehicleID(vehicleID);
        assertEquals(vehicleID, journeyService.getVehicleID());
    }
}
