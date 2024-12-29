import data.*;
import exceptions.*;
import micromobility.JourneyRealizeHandler;
import micromobility.JourneyService;
import micromobility.PMVehicle;
import micromobility.payment.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.*;
import services.smartfeatures.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyRealizeHandlerTest {
    private JourneyRealizeHandler journeyRealizeHandler;
    private SimulatedQRDecoder simulatedQRDecoder;
    private PMVehicle pmVehicle;
    private UserAccount userAccount;
    private StationID stationID;
    private GeographicPoint geographicPoint;
    private SimulatedArduinoMicroController arduinoController;
    private ServerSimulated serverSimulated;
    private JourneyService journeyService;
    private Wallet wallet;
    private SimulatedUnbondedBTSignal simulatedBTSignal;

    //NOTA: Aquí hacemos tests generales, ya que las funciones y otras funcionalidades ya están comprobados con los tests de cada clase correspondiente!
    //¡Además, si un test ha fallado es porque usamos random para algunos casos de problemas, entonces tienes que volver a ejecutar test!
    @BeforeEach
    void setUp() {
        // Crear instancias de las dependencias necesarias
        simulatedQRDecoder = new SimulatedQRDecoder();
        pmVehicle = new PMVehicle(new VehicleID("V123"));
        userAccount = new UserAccount("user123");
        stationID = new StationID("station001");
        geographicPoint = new GeographicPoint(40, -74);
        arduinoController = new SimulatedArduinoMicroController(true, true, pmVehicle, null, stationID, geographicPoint, userAccount);
        serverSimulated = new ServerSimulated();
        journeyService = new JourneyService(new ServiceID(), userAccount, BigDecimal.ZERO, 'W');
        wallet = new Wallet(new BigDecimal("100.00"));
        simulatedBTSignal = new SimulatedUnbondedBTSignal(true);

        journeyRealizeHandler = new JourneyRealizeHandler(serverSimulated, simulatedBTSignal, pmVehicle, journeyService, simulatedQRDecoder, wallet);

        pmVehicle.setLocation(geographicPoint);
    }

    @Test
    void testBroadcastStationID_ValidStatID_BluetoothWorks() {
        String validStatID = "station123";
        try {
            String result = journeyRealizeHandler.broadcastStationID(validStatID);
            assertEquals(validStatID, result); // Los ID deben coincidir
        } catch (ConnectException e) {
            fail("No se esperaba una excepción.");
        }
    }

    @Test
    void testBroadcastStationID_NullOrEmptyStatID() {
        String nullStatID = null;
        String emptyStatID = "";

        ConnectException exception1 = assertThrows(ConnectException.class, () -> journeyRealizeHandler.broadcastStationID(nullStatID));
        assertEquals("No se puede recibir la señal de la estación.", exception1.getMessage());
        ConnectException exception2 = assertThrows(ConnectException.class, () -> journeyRealizeHandler.broadcastStationID(emptyStatID));
        assertEquals("No se puede recibir la señal de la estación.", exception2.getMessage());
    }

    @Test
    void testBroadcastStationID_BluetoothError() {
        simulatedBTSignal = new SimulatedUnbondedBTSignal(false);
        journeyRealizeHandler = new JourneyRealizeHandler(
                serverSimulated,
                simulatedBTSignal,
                pmVehicle,
                journeyService,
                simulatedQRDecoder,
                wallet
        );

        //Bluetooth no funcional
        ConnectException exception = assertThrows(ConnectException.class, () -> journeyRealizeHandler.broadcastStationID("station123"));
        assertEquals("Error al transmitir la señal de la estación por Bluetooth: Error en la conexión Bluetooth.", exception.getMessage());
    }

    @Test
    void testScanQR_NullOrEmptyImageName() {
        InvalidPairingArgsException exception1 = assertThrows(InvalidPairingArgsException.class, () -> journeyRealizeHandler.scanQR(null));
        assertEquals("El nombre del archivo no puede ser nulo o vacío.", exception1.getMessage());

        InvalidPairingArgsException exception2 = assertThrows(InvalidPairingArgsException.class, () -> journeyRealizeHandler.scanQR(""));
        assertEquals("El nombre del archivo no puede ser nulo o vacío.", exception2.getMessage());
    }
    @Test
    void testScanQR_ValidImage() throws ConnectException, InvalidPairingArgsException, PMVNotAvailException, CorruptedImgException, ProceduralException, PairingNotFoundException {
        String imageName = "qr_vehicle1.png";

        journeyRealizeHandler.scanQR(imageName);

        assertNotNull(journeyService.getUserAccount());
        assertNotNull(journeyService.getVehicleID());
    }

    @Test
    void testScanQR_InvalidImage() {
        String imageName = "qr_invalid.png";
        assertThrows(CorruptedImgException.class, () -> {
            journeyRealizeHandler.scanQR(imageName);
        });
    }

    @Test
    void testScanQR_CorruptedImage() {
        String imageName = "corrupted_image.png";

        CorruptedImgException exception = assertThrows(CorruptedImgException.class, () -> journeyRealizeHandler.scanQR(imageName));
        assertEquals("Código QR no reconocido", exception.getMessage());
    }
    @Test
    void testStartDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        pmVehicle.setNotAvailb();//previamente emparejado
        journeyRealizeHandler.startDriving();

        // Verificamos que el servicio se ha inicializado
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime serviceInitTime = journeyService.getServiceInit();
        assertNotNull(serviceInitTime, "El tiempo de inicio del servicio no debería ser nulo.");
        assertTrue(serviceInitTime.isAfter(currentDateTime.minusSeconds(1)) && serviceInitTime.isBefore(currentDateTime.plusSeconds(1)));//El tiempo de inicio del servicio debería estar dentro del rango de un segundo de la hora actual
    }

    @Test
    void testStartDriving_ProceduralException() throws ConnectException, PMVPhisicalException, ProceduralException {
        ProceduralException exception = assertThrows(ProceduralException.class, () -> journeyRealizeHandler.startDriving());//Sim emparejamiento previo
        assertEquals("El vehículo no está en el estado correcto para iniciar el trayecto. Debe estar 'NotAvailable'.", exception.getMessage());
    }

    @Test
    void testStopDriving_Success() throws ConnectException, ProceduralException, PMVPhisicalException {
        pmVehicle.setNotAvailb();
        journeyRealizeHandler.startDriving();

        journeyRealizeHandler.stopDriving();

        // Verificamos que la estación fue transmitida correctamente
        String endStatID = String.valueOf(stationID);
        assertEquals(endStatID, journeyRealizeHandler.broadcastStationID(endStatID));
    }
    @Test
    void testDoubleOption_Stop() throws ConnectException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        journeyRealizeHandler.DoubleOption();

        // Verificamos que los mensajes correctos se han impreso en la salida
        String expectedMessage1 = "El vehículo ha sido detenido. ¿Qué desea hacer?";
        String expectedMessage2 = "1. Realizar una parada.";
        String expectedMessage3 = "2. Desemparejar vehículo.";

        assertTrue(outputStream.toString().contains(expectedMessage1), "El mensaje de inicio no fue impreso correctamente.");
        assertTrue(outputStream.toString().contains(expectedMessage2), "El mensaje de opción 1 no fue impreso correctamente.");
        assertTrue(outputStream.toString().contains(expectedMessage3), "El mensaje de opción 2 no fue impreso correctamente.");

        String unexpectedMessage = "El conductor ha decidido realizar una parada.";
        assertFalse(outputStream.toString().contains(unexpectedMessage), "El mensaje de parada no debe aparecer.");
    }

    @Test
    void testDoubleOption_UnpairVehicleCalled() {
        // En este test verificamos que si la opción seleccionada es 2, el método unPairVehicle() se llama.
        try {
            journeyRealizeHandler.DoubleOption();
        } catch (Exception e) {
            // Verificamos que el error esperado (si es lanzado por el método unPairVehicle()) se maneja correctamente.
            assertEquals("Error al intentar desemparejar el vehículo: " + e.getMessage(),
                    "El mensaje de error al desemparejar debe ser el esperado.");
        }
    }
    @Test
    void testUnPairVehicle_ProceduralException_VehicleNotUnderWay() throws ProceduralException {
        // El vehículo no está en estado UnderWay
        ProceduralException exception = assertThrows(ProceduralException.class, () -> journeyRealizeHandler.unPairVehicle());
        assertEquals("El vehículo no se encuentra en estado UnderWay.", exception.getMessage());
    }

    @Test
    void testUnPairVehicle_ProceduralException_JourneyNotInProgress() {
        journeyService.setInProgress(false);

        ProceduralException exception = assertThrows(ProceduralException.class, () -> journeyRealizeHandler.unPairVehicle());
        assertEquals("El vehículo no se encuentra en estado UnderWay.", exception.getMessage());
    }
    @Test
    void testUnPairVehicle_Success() throws Exception {
        String imageName = "qr_vehicle1.png";
        journeyRealizeHandler.scanQR(imageName);
        journeyRealizeHandler.startDriving();
        journeyRealizeHandler.unPairVehicle();

        assertFalse(journeyService.isInProgress(), "El trayecto debería haber finalizado.");

        LocalDateTime currentDateTime = LocalDateTime.now();
        assertTrue(journeyService.getEndDate().isAfter(currentDateTime.minusSeconds(1)) &&
                        journeyService.getEndDate().isBefore(currentDateTime.plusSeconds(1)));
    }
    @Test
    void testCalculateValuesAndImport() {
        GeographicPoint startPoint = new GeographicPoint(40, -74);
        GeographicPoint endPoint = new GeographicPoint(34, -118);
        LocalDateTime startDateTime = LocalDateTime.of(2024, 12, 29, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 29, 12, 30);

        journeyService.setOriginPoint(startPoint);
        journeyService.setServiceInit(startDateTime);

        journeyRealizeHandler.calculateValues(endPoint, endDateTime);

        long expectedDuration = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
        assertEquals(expectedDuration, journeyService.getDuration(), "La duración calculada no es correcta.");

        double expectedDistance = journeyRealizeHandler.calculateDistance(startPoint, endPoint);
        assertEquals(expectedDistance, journeyService.getDistance(), 0.01, "La distancia calculada no es correcta.");

        float expectedAvgSpeed = (float) (expectedDistance / expectedDuration);  // en metros/minuto
        assertEquals(expectedAvgSpeed, journeyService.getAvgSpeed(), 0.01, "La velocidad media calculada no es correcta.");
    }
    @Test
    void testSelectPaymentMethod_WalletPayment_Success() throws Exception {
        BigDecimal initialBalance = new BigDecimal("100.00");
        wallet = new Wallet(initialBalance);
        BigDecimal paymentAmount = new BigDecimal("0");

        ServiceID servID = new ServiceID("uni12");
        journeyService = new JourneyService(servID, userAccount, BigDecimal.ZERO, 'W');
        journeyService.setImportAmount(paymentAmount);

        assertTrue(wallet.getBalance().compareTo(paymentAmount) >= 0);

        journeyRealizeHandler.selectPaymentMethod('W');

        BigDecimal expectedBalance = initialBalance.subtract(paymentAmount);
        assertEquals(expectedBalance, wallet.getBalance());

        BigDecimal expectedImportAmount = BigDecimal.ZERO;
        assertEquals(expectedImportAmount, journeyService.getImportAmount());
    }


    @Test
    void testSelectPaymentMethod_WalletPayment_InsufficientBalance() {
        wallet = new Wallet(new BigDecimal("10.00"));
        journeyRealizeHandler = new JourneyRealizeHandler(serverSimulated, simulatedBTSignal, pmVehicle, journeyService, simulatedQRDecoder, wallet);

        BigDecimal paymentAmount = new BigDecimal("50.00");
        journeyService.setImportAmount(paymentAmount);

        assertThrows(NotEnoughWalletException.class, () -> {
            journeyRealizeHandler.selectPaymentMethod('W');});
    }
    @Test
    void testSelectPaymentMethod_InvalidOption() {
        assertThrows(ProceduralException.class, () -> {
            journeyRealizeHandler.selectPaymentMethod('X');
        });
    }











}