        package handler;

        import exceptions.*;
        import services.*;
        import services.smartfeatures.*;
        import micromobility.*;
        import domain.*;
        import data.*;

        import java.io.IOException;
        import java.time.LocalDateTime;
        import java.math.BigDecimal;
        import java.awt.image.BufferedImage;
        import javax.imageio.ImageIO;
        import java.io.File;


        public class JourneyRealizeHandler {

            private Server server;
            private UnbondedBTSignal btSignal;
            private PMVehicle pmVehicle;
            private JourneyService journeyService;
            private QRDecoder qrDecoder;
            private AppWallet appWallet;

            // Crear objetos previamente
            private UserAccount user = new UserAccount("user123"); // ID de cuenta de usuario
            private StationID station = new StationID("station001");  // ID de estación
            private GeographicPoint location = new GeographicPoint(40.7128f, -74.0060f); // Coordenadas de ubicación

            public JourneyRealizeHandler(Server server, UnbondedBTSignal btSignal, PMVehicle pmVehicle, JourneyService journeyService, QRDecoder qrDecoder, AppWallet appWallet) {
                this.server = server;
                this.btSignal = btSignal;
                this.pmVehicle = pmVehicle;
                this.journeyService = journeyService;
                this.qrDecoder = qrDecoder; // Cambiado para evitar `null`
                this.appWallet = appWallet;
            }


            public String broadcastStationID(String statID) throws ConnectException {
                if (statID == null || statID.isEmpty()) {
                    throw new ConnectException("No se puede recibir la señal de la estación.");
                }
                System.out.println("ID de estación recibido: " + statID);
                return statID;
            }


            public void scanQR(String imageName) throws ConnectException, InvalidPairingArgsException, PMVNotAvailException, CorruptedImgException {
                if (imageName == null || imageName.trim().isEmpty()) {
                    throw new InvalidPairingArgsException("El nombre del archivo no puede ser nulo o vacío.");
                }

                try {
                    // Usar el nombre del archivo para obtener el ID del vehículo
                    VehicleID vehID = qrDecoder.getVehicleIDByImg(imageName);

                    // Verificar disponibilidad del vehículo
                    server.checkPMVAvail(vehID);

                    // Comprobar la conexión Bluetooth
                    btSignal.BTbroadcast();

                    pmVehicle.setNotAvailb();

                    journeyService = new JourneyService();
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    journeyService.setServiceInit(currentDateTime); // Iniciar el trayecto con la hora actual
                    // Establecer la estación de origen y ubicación
                    journeyService.setOriginPoint(location); // Ubicación del conductor
                    journeyService.setOrgStatID(station); // Estación de origen
                    // Asociar los datos con el JourneyService
                    journeyService.setUserAccount(user);  // Asociar la cuenta de usuario
                    journeyService.setVehicleID(vehID);  // Asociar el vehículo

                    server.registerPairing(user,vehID,station,location,currentDateTime);


                    // Completar flujo
                    System.out.println("Emparejamiento completado con el vehículo: " + vehID.getVehicleId());
                    System.out.println("Detalles del emparejamiento: Usuario " + user.getAccountId() + ", Estación " + station.getId() + ", Ubicación " + location);
                } catch (PMVNotAvailException | CorruptedImgException | ConnectException e) {
                    throw e;
                }
            }
            public void startDriving() throws ConnectException {
                pmVehicle.setUnderWay(); // Cambiar el estado del vehículo
                journeyService.setServiceInit(LocalDateTime.now()); // Iniciar el trayecto
                System.out.println("Trayecto iniciado.");
            }

            public void stopDriving() throws ConnectException {
                pmVehicle.setAvailb(); // Cambiar el estado del vehículo
                journeyService.setServiceFinish(LocalDateTime.now(), 10.5f, 30, 21.0f, new BigDecimal("15.00")); // Finalizar el trayecto
                System.out.println("Trayecto finalizado.");
            }

            // Métodos internos para cálculos
            private void calculateValues(double distance, int duration, float avgSpeed) {
                // Aquí iría la lógica para calcular valores del trayecto
                System.out.println("Calculando valores del trayecto...");
            }

            private void calculateImport(float distance, int duration, float avgSpeed) {
                // Aquí iría la lógica para calcular el importe del servicio
                System.out.println("Calculando el importe...");
            }
        }
        // Evento de entrada para seleccionar método de pago
            public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
                if (opt != 'C' && opt != 'D' && opt != 'W' && opt != 'B') { // Ejemplo: 'C'= Crédito, 'D'= Débito, 'W'= Monedero, 'B'= Billetera digital
                    throw new ProceduralException("Método de pago no válido.");
                }

                System.out.println("Método de pago seleccionado: " + opt);

                // Obtener el importe a pagar desde el JourneyService (simulación)
                BigDecimal amountToPay = journeyService.getImport(); // Supongamos que este método devuelve el importe del trayecto

                if (opt == 'W') { // Monedero en la app
                    realizePayment(amountToPay);
                } else {
                    // Lógica de conexión con otros servicios de pago, si corresponde
                    System.out.println("Procesando pago con método externo...");
                }
            }

            // Operación interna para realizar el pago desde el monedero de la app
            private void realizePayment(BigDecimal imp) throws NotEnoughWalletException {
                if (appWallet.getBalance().compareTo(imp) < 0) {
                    throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
                }

                // Deduce el importe del saldo del monedero
                appWallet.deduct(imp);
                System.out.println("Pago realizado con éxito. Importe: " + imp);
            }
