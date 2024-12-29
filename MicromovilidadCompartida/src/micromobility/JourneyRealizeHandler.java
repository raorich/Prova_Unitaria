        package micromobility;

        import exceptions.*;
        import micromobility.payment.Wallet;
        import services.*;
        import services.smartfeatures.*;
        import micromobility.*;
        import data.*;

        import java.time.LocalDateTime;
        import java.math.BigDecimal;


        public class JourneyRealizeHandler {

            private Server server;
            private UnbondedBTSignal btSignal;
            private PMVehicle pmVehicle;
            private JourneyService journeyService;
            private QRDecoder qrDecoder;
            private Wallet wallet;
            private ArduinoMicroController arduinoController;


            // Crear objetos previamente
            private UserAccount user = new UserAccount("user123");
            private StationID station = new StationID("station001");
            private GeographicPoint location = new GeographicPoint(40.7128f, -74.0060f);

            public JourneyRealizeHandler(Server server, UnbondedBTSignal btSignal, PMVehicle pmVehicle, JourneyService journeyService, QRDecoder qrDecoder, Wallet wallet) {
                this.server = server;
                this.btSignal = btSignal;
                this.pmVehicle = pmVehicle;
                this.journeyService = journeyService;
                this.qrDecoder = qrDecoder;
                this.wallet = wallet;
                this.arduinoController = arduinoController;

                this.arduinoController = new SimulatedArduinoMicroController(
                        true, true, pmVehicle, journeyService, station, location, user);
            }


            public String broadcastStationID(String statID) throws ConnectException {
                if (statID == null || statID.isEmpty()) {
                    throw new ConnectException("No se puede recibir la señal de la estación.");
                }

                // Simulamos la emisión de la señal por BT
                try {
                    btSignal.BTbroadcast();
                } catch (ConnectException e) {
                    throw new ConnectException("Error al transmitir la señal de la estación por Bluetooth: " + e.getMessage());
                }

                System.out.println("ID de estación recibido: " + statID);
                return statID;
            }

            public void scanQR(String imageName) throws ConnectException, InvalidPairingArgsException, PMVNotAvailException, CorruptedImgException, ProceduralException, PairingNotFoundException {
                if (imageName == null || imageName.trim().isEmpty()) {
                    throw new InvalidPairingArgsException("El nombre del archivo no puede ser nulo o vacío.");
                }

                // Usar el nombre del archivo para obtener el ID del vehículo
                VehicleID vehID = qrDecoder.getVehicleIDByImg(imageName);

                // Verificar disponibilidad del vehículo
                server.checkPMVAvail(vehID);

                // Comprobar la conexión Bluetooth
                arduinoController.setBTconnection();

                // Cambiar el estado del vehículo a no disponible
                pmVehicle.setNotAvailb();

                // Configuración del trayecto
                LocalDateTime currentDateTime = LocalDateTime.now();
                journeyService.setServiceInit(currentDateTime); // Iniciar el trayecto con la hora actual
                journeyService.setOriginPoint(location); // Establecer la ubicación del conductor
                journeyService.setOrgStatID(station); // Establecer la estación de origen
                journeyService.setUserAccount(user); // Asociar la cuenta de usuario
                journeyService.setVehicleID(vehID); // Asociar el vehículo

                // Registrar
                server.registerPairing(user, vehID, station, location, currentDateTime);

                System.out.println("Emparejamiento completado con el vehículo: " + vehID.getVehicleId());
                System.out.println("Detalles del emparejamiento: Usuario " + user.getAccountId() + ", Estación " + station.getId() + ", Ubicación " + location);
            }

            public void startDriving() throws ConnectException, PMVPhisicalException, ProceduralException {
                arduinoController.startDriving();
                LocalDateTime currentDateTime = LocalDateTime.now();
                journeyService.setServiceInit(currentDateTime);

            }

            public void stopDriving() throws ConnectException, ProceduralException, PMVPhisicalException {
                String endStatID =broadcastStationID(station.getId());
                arduinoController.stopDriving();
                DoubleOption();
            }
            public void DoubleOption() throws ConnectException {
                // Simulamos que el conductor elige entre detener el vehículo temporalmente o desemparejarlo
                System.out.println("El vehículo ha sido detenido. ¿Qué desea hacer?");
                System.out.println("1. Realizar una parada.");
                System.out.println("2. Desemparejar vehículo.");

                // Aquí simulamos la elección del conductor
                int option = 2;

                if (option == 1) {
                    System.out.println("El conductor ha decidido realizar una parada.");
                } else if (option == 2) {
                    try {
                        unPairVehicle();
                    } catch (Exception e) {
                        System.out.println("Error al intentar desemparejar el vehículo: " + e.getMessage());
                    }
                } else {
                    System.out.println("Opción no válida. El vehículo permanece detenido.");
                }
            }
            public void unPairVehicle() throws ConnectException, ProceduralException, InvalidPairingArgsException, PairingNotFoundException {
                // Emitir ID de la estación por BT
                String endStatID = broadcastStationID(station.getId());
                if (endStatID == null || endStatID.isEmpty()) {
                    throw new ProceduralException("La estación de PMV no está identificada correctamente.");
                }
                if (pmVehicle.getState() != PMVehicle.PMVState.UnderWay) {
                    throw new ProceduralException("El vehículo no se encuentra en estado UnderWay.");
                }
                if (!journeyService.isInProgress()) {
                    throw new ProceduralException("El trayecto no está en curso. No se puede proceder al desemparejamiento.");
                }
                if (journeyService.getUserAccount() == null) {
                    throw new PairingNotFoundException("No se ha encontrado el emparejamiento entre el vehículo y la cuenta de usuario.");
                }
                try {
                    // Obtener el VehicleID para el desemparejamiento
                    VehicleID vehicleID = pmVehicle.getVehicleID();

                    LocalDateTime currentDateTime = LocalDateTime.now();
                    GeographicPoint destination = pmVehicle.getLocation();
                    pmVehicle.setLocation(destination);

                    // Actualizar atributos
                    journeyService.setEndPoint(destination);
                    journeyService.setServiceFinish(currentDateTime);

                    // Calcular valores
                    calculateValues(destination, currentDateTime);
                    calculateImport(journeyService.getDistance(), journeyService.getDuration(), journeyService.getAvgSpeed(), currentDateTime);

                    // Llamamos al método para finalizar el emparejamiento
                    server.stopPairing(user, vehicleID, station, location, currentDateTime, journeyService.getAvgSpeed(), journeyService.getDistance(), journeyService.getDuration(), journeyService.getImportAmount());
                    server.unPairRegisterService(journeyService);
                    pmVehicle.setAvailb();
                    server.removeJourneyFromCache(journeyService);
                    System.out.println("El desemparejamiento ha finalizado correctamente.");

                    ServiceID newServiceID = new ServiceID();
                    journeyService = new JourneyService(newServiceID, user, BigDecimal.ZERO, 'W');  // Crear el objeto JourneyService con el ServiceID
                } catch (ConnectException | InvalidPairingArgsException | PairingNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ConnectException("Hubo un error al intentar realizar el desemparejamiento: " + e.getMessage());
                }
            }

            public void calculateValues(GeographicPoint destination, LocalDateTime endDateTime) {
                GeographicPoint startPoint = journeyService.getOriginPoint();
                LocalDateTime startDateTime = journeyService.getServiceInit();

                // Calcular la duración
                long durationMinutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
                journeyService.setDuration((int) durationMinutes);

                // Calcular la distancia entre el punto de origen y el punto final
                double distance = calculateDistance(startPoint, destination);
                journeyService.setDistance((float) distance);

                // Calcular la velocidad media
                float avgSpeed = (float) (distance / durationMinutes);  // en metros/minuto
                journeyService.setAvgSpeed(avgSpeed);

                System.out.println("Duración: " + durationMinutes + " minutos.");
                System.out.println("Distancia: " + distance + " metros.");
                System.out.println("Velocidad Media: " + avgSpeed + " m/min.");
            }

            public double calculateDistance(GeographicPoint start, GeographicPoint end) {
                final double R = 6371e3;
                double lat1 = Math.toRadians(start.getLatitude());
                double lat2 = Math.toRadians(end.getLatitude());
                double deltaLat = Math.toRadians(end.getLatitude() - start.getLatitude());
                double deltaLon = Math.toRadians(end.getLongitude() - start.getLongitude());

                double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                // Distancia
                return R * c;
            }


            void calculateImport(float distance, int duration, float avgSpeed, LocalDateTime date) {

                BigDecimal importAmount;


                BigDecimal distanceRate = new BigDecimal(0.5);  // Ejemplo: 0.5 unidades por km
                BigDecimal durationRate = new BigDecimal(0.1);  // Ejemplo: 0.1 unidades por minuto

                double distanceInKm = distance / 1000.0;
                BigDecimal distanceCost = new BigDecimal(distanceInKm).multiply(distanceRate);

                BigDecimal durationCost = new BigDecimal(duration).multiply(durationRate);  // Cálculo de costo por duración

                // Importe total = costo por distancia + costo por duración
                importAmount = distanceCost.add(durationCost);

                // Actualizamos el importe
                journeyService.setImportAmount(importAmount);

                System.out.println("Importe calculado: " + importAmount);
            }

            public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
                // Verificar si el método de pago es válido
                if (opt != 'C' && opt != 'D' && opt != 'W' && opt != 'B') {
                    throw new ProceduralException("Método de pago no válido.");
                }
                System.out.println("Método de pago seleccionado: " + opt);
                BigDecimal amountToPay = journeyService.getImportAmount();
                if (opt == 'W') { // Monedero en la app
                    realizePayment(amountToPay);
                    server.registerPayment(journeyService.getServiceID(),user,amountToPay,opt);
                } else if (opt == 'C' || opt == 'D' || opt == 'B') { // Tarjeta, Débito o Bizum
                    System.out.println("Procesando pago con método externo...");
                } else {
                    throw new ProceduralException("Método de pago no reconocido. No se pudo procesar el pago.");
                }
            }
            private void realizePayment(BigDecimal imp) throws NotEnoughWalletException, ConnectException {
                if (wallet.getBalance().compareTo(imp) < 0) {
                    throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
                }

                // Deduce el importe del saldo del monedero
                wallet.applyDeduction(imp);
                System.out.println("Pago realizado con éxito. Importe: " + imp);
                arduinoController.undoBTconnection();
            }
        }

