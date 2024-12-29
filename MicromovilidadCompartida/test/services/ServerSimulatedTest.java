package services;

import data.*;
import domain.JourneyService;
import exceptions.*;
import exceptions.PairingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.QRDecoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServerSimulatedTest {
    private JourneyService journeyService;
    private LocalDateTime validStartDate;
    private LocalDateTime validEndDate;
    private StationID validStationID;
    private GeographicPoint validOrigin;
    private GeographicPoint validDestination;
    private UserAccount validUserAccount;
    private VehicleID validVehicleID;
    private BigDecimal validImportAmount;

    @BeforeEach
    void setUp() {
        validStartDate = LocalDateTime.now();
        validEndDate = validStartDate.plusMinutes(30);
        validStationID = new StationID("ST123");
        validOrigin = new GeographicPoint(40.7128f, -74.0060f);
        validDestination = new GeographicPoint(40.7306f, -73.9352f);
        validUserAccount = new UserAccount("user123");
        validVehicleID = new VehicleID("V123");
        validImportAmount = new BigDecimal("15.00");

        journeyService = new JourneyService(null, validUserAccount, validImportAmount, 'W');
    }

    @Test
    void testSetServiceInit_Success() {
        assertDoesNotThrow(() -> journeyService.setServiceInit(validStartDate));
        assertEquals(validStartDate, journeyService.getServiceInit());
        assertTrue(journeyService.isInProgress());
    }

    @Test
    void testSetServiceInit_Failure_NullDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setServiceInit(null));
        assertEquals("La fecha de inicio no puede ser nula.", exception.getMessage());
    }

    @Test
    void testSetServiceFinish_Success() {
        journeyService.setServiceInit(validStartDate);
        assertDoesNotThrow(() -> journeyService.setServiceFinish(validEndDate));
        assertEquals(validEndDate, journeyService.getServiceFinish());
        assertFalse(journeyService.isInProgress());
    }

    @Test
    void testSetServiceFinish_Failure_NullDate() {
        journeyService.setServiceInit(validStartDate);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setServiceFinish(null));
        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio.", exception.getMessage());
    }

    @Test
    void testSetServiceFinish_Failure_EndBeforeStart() {
        journeyService.setServiceInit(validStartDate);
        LocalDateTime invalidEndDate = validStartDate.minusMinutes(10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setServiceFinish(invalidEndDate));
        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio.", exception.getMessage());
    }

    @Test
    void testSetDistance_Success() {
        float validDistance = 5000.0f; // 5 km
        assertDoesNotThrow(() -> journeyService.setDistance(validDistance));
        assertEquals(validDistance, journeyService.getDistance());
    }

    @Test
    void testSetDistance_Failure_NegativeValue() {
        float invalidDistance = -100.0f;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setDistance(invalidDistance));
        assertEquals("La distancia no puede ser negativa.", exception.getMessage());
    }

    @Test
    void testSetDuration_Success() {
        int validDuration = 30; // 30 minutes
        assertDoesNotThrow(() -> journeyService.setDuration(validDuration));
        assertEquals(validDuration, journeyService.getDuration());
    }

    @Test
    void testSetDuration_Failure_NegativeValue() {
        int invalidDuration = -5;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setDuration(invalidDuration));
        assertEquals("La duración no puede ser negativa.", exception.getMessage());
    }

    @Test
    void testSetAvgSpeed_Success() {
        float validAvgSpeed = 20.5f; // 20.5 km/h
        assertDoesNotThrow(() -> journeyService.setAvgSpeed(validAvgSpeed));
        assertEquals(validAvgSpeed, journeyService.getAvgSpeed());
    }

    @Test
    void testSetAvgSpeed_Failure_NegativeValue() {
        float invalidAvgSpeed = -10.0f;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setAvgSpeed(invalidAvgSpeed));
        assertEquals("La velocidad promedio no puede ser negativa.", exception.getMessage());
    }

    @Test
    void testSetImportAmount_Success() {
        assertDoesNotThrow(() -> journeyService.setImportAmount(validImportAmount));
        assertEquals(validImportAmount, journeyService.getImportAmount());
    }

    @Test
    void testSetImportAmount_Failure_NegativeValue() {
        BigDecimal invalidImportAmount = new BigDecimal("-1.00");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> journeyService.setImportAmount(invalidImportAmount));
        assertEquals("El importe no puede ser negativo.", exception.getMessage());
    }

    @Test
    void testSetAndGetOtherAttributes() {
        journeyService.setOrgStatID(validStationID);
        journeyService.setOriginPoint(validOrigin);
        journeyService.setEndPoint(validDestination);
        journeyService.setVehicleID(validVehicleID);

        assertEquals(validStationID, journeyService.getOrgStatID());
        assertEquals(validOrigin, journeyService.getOriginPoint());
        assertEquals(validDestination, journeyService.getEndPoint());
        assertEquals(validVehicleID, journeyService.getVehicleID());
    }
    @Test
    void testInvalidQRCode() {
        ServerSimulated server = new ServerSimulated();
        QRDecoder qrDecoder = server.getQRDecoder();

        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg("qr_invalid.png"));
    }

}
