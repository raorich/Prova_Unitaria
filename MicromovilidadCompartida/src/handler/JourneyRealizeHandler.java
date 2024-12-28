        package handler;

        import exceptions.*;
        import micromobility.payment.Wallet;
        import services.*;
        import services.smartfeatures.*;
        import micromobility.*;
        import domain.*;
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
            private UserAccount user = new UserAccount("user123"); // ID de cuenta de usuario
            private StationID station = new StationID("station001");  // ID de estación
            private GeographicPoint location = new GeographicPoint(40.7128f, -74.0060f); // Coordenadas de ubicación

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
                //journeyService = new JourneyService();
                LocalDateTime currentDateTime = LocalDateTime.now();
                journeyService.setServiceInit(currentDateTime); // Iniciar el trayecto con la hora actual
                journeyService.setOriginPoint(location); // Establecer la ubicación del conductor
                journeyService.setOrgStatID(station); // Establecer la estación de origen
                journeyService.setUserAccount(user); // Asociar la cuenta de usuario
                journeyService.setVehicleID(vehID); // Asociar el vehículo

                // Realizar el emparejamiento
                server.registerPairing(user, vehID, station, location, currentDateTime);

                System.out.println("Emparejamiento completado con el vehículo: " + vehID.getVehicleId());
                System.out.println("Detalles del emparejamiento: Usuario " + user.getAccountId() + ", Estación " + station.getId() + ", Ubicación " + location);
            }

            // Método para iniciar el trayecto (llamar al método de Arduino)
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

                // Aquí simulamos la elección del conductor (en un escenario real, este valor podría venir de un input del conductor)
                int option = 2; // Supongamos que el conductor elige la opción 2 (desemparejar el vehículo)

                if (option == 1) {
                    // El conductor decide realizar una parada, no cambiamos el estado del vehículo
                    System.out.println("El conductor ha decidido realizar una parada.");
                    // El vehículo permanece en estado 'UnderWay' (el trayecto está en progreso)
                } else if (option == 2) {
                    // El conductor decide desemparejar el vehículo
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

                // Comprobamos si la estación está correctamente identificada y el vehículo está estacionado en la zona delimitada.
                if (endStatID == null || endStatID.isEmpty()) {
                    throw new ProceduralException("La estación de PMV no está identificada correctamente.");
                }

                // Comprobamos si el vehículo está en estado "UnderWay" (en uso)
                if (pmVehicle.getState() != PMVehicle.PMVState.UnderWay) {
                    throw new ProceduralException("El vehículo no se encuentra en estado UnderWay.");
                }

                // Verificamos si el trayecto está en curso
                if (!journeyService.isInProgress()) {
                    throw new ProceduralException("El trayecto no está en curso. No se puede proceder al desemparejamiento.");
                }

                // Comprobamos si existe el emparejamiento entre el vehículo y el usuario
                if (journeyService.getUserAccount() == null) {
                    throw new PairingNotFoundException("No se ha encontrado el emparejamiento entre el vehículo y la cuenta de usuario.");
                }

                // Procedemos al desemparejamiento
                try {
                    // Obtener el VehicleID para el desemparejamiento
                    VehicleID vehicleID = pmVehicle.getVehicleID();

                    LocalDateTime currentDateTime = LocalDateTime.now();
                    GeographicPoint destination = pmVehicle.getLocation();
                    pmVehicle.setLocation(destination);

                    // Actualizar atributos del servicio de trayecto (JourneyService)
                    journeyService.setEndPoint(destination);  // Establecer el punto final
                    journeyService.setServiceFinish(currentDateTime);

                    // Calcular valores como la distancia, duración y velocidad media
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
                    // Capturamos las excepciones relacionadas con el emparejamiento y la conexión
                    throw e;
                } catch (Exception e) {
                    // Capturamos cualquier otra excepción no anticipada
                    throw new ConnectException("Hubo un error al intentar realizar el desemparejamiento: " + e.getMessage());
                }
            }

            // Métodos internos para cálculos
            private void calculateValues(GeographicPoint destination, LocalDateTime endDateTime) {
                // Obtén el punto de inicio y la hora de inicio desde journeyService
                GeographicPoint startPoint = journeyService.getOriginPoint();  // Punto de origen (ubicación del conductor al inicio)
                LocalDateTime startDateTime = journeyService.getServiceInit();  // Hora de inicio del trayecto

                // Calcular la duración (en minutos)
                long durationMinutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
                journeyService.setDuration((int) durationMinutes);

                // Calcular la distancia entre el punto de origen y el punto final (en metros)
                double distance = calculateDistance(startPoint, destination);
                journeyService.setDistance((float) distance);

                // Calcular la velocidad media (distancia / duración)
                float avgSpeed = (float) (distance / durationMinutes);  // en metros/minuto
                journeyService.setAvgSpeed(avgSpeed);

                // Imprimir información para depuración (puedes eliminarlo en producción)
                System.out.println("Duración: " + durationMinutes + " minutos.");
                System.out.println("Distancia: " + distance + " metros.");
                System.out.println("Velocidad Media: " + avgSpeed + " m/min.");
            }

            // Método para calcular la distancia entre dos puntos geográficos (en metros)
            private double calculateDistance(GeographicPoint start, GeographicPoint end) {
                // Utilizamos una fórmula simple para calcular la distancia entre dos puntos geográficos
                final double R = 6371e3; // Radio de la Tierra en metros
                double lat1 = Math.toRadians(start.getLatitude());
                double lat2 = Math.toRadians(end.getLatitude());
                double deltaLat = Math.toRadians(end.getLatitude() - start.getLatitude());
                double deltaLon = Math.toRadians(end.getLongitude() - start.getLongitude());

                // Fórmula Haversine para calcular la distancia entre dos puntos en la esfera
                double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                // Distancia en metros
                return R * c;
            }


            private void calculateImport(float distance, int duration, float avgSpeed, LocalDateTime date) {
                // Aquí puedes implementar la lógica específica para calcular el importe
                // Ejemplo de un cálculo simple que depende de la distancia y la duración

                BigDecimal importAmount;

                // Tarifa base por distancia (por ejemplo, 0.5 unidades por kilómetro)
                BigDecimal distanceRate = new BigDecimal(0.5);  // Ejemplo: 0.5 unidades por km
                // Tarifa por minuto (por ejemplo, 0.1 unidades por minuto)
                BigDecimal durationRate = new BigDecimal(0.1);  // Ejemplo: 0.1 unidades por minuto

                // Convertir la distancia a kilómetros
                double distanceInKm = distance / 1000.0;  // Convertir metros a kilómetros
                BigDecimal distanceCost = new BigDecimal(distanceInKm).multiply(distanceRate);  // Cálculo de costo por distancia

                // Cálculo de costo por duración
                BigDecimal durationCost = new BigDecimal(duration).multiply(durationRate);  // Cálculo de costo por duración

                // Importe total = costo por distancia + costo por duración
                importAmount = distanceCost.add(durationCost);

                // Actualizamos el importe en el JourneyService
                journeyService.setImportAmount(importAmount);

                // Imprimir información para depuración (puedes eliminarlo en producción)
                System.out.println("Importe calculado: " + importAmount);
            }



            // Evento de entrada para seleccionar método de pago
            public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
                // Verificar si el método de pago es válido
                if (opt != 'C' && opt != 'D' && opt != 'W' && opt != 'B') {
                    throw new ProceduralException("Método de pago no válido.");
                }
                // Si el método de pago es válido, imprimir el mensaje correspondiente
                System.out.println("Método de pago seleccionado: " + opt);
                // Obtener el importe a pagar desde el JourneyService
                BigDecimal amountToPay = journeyService.getImportAmount();
                // Lógica para procesar el pago según el método seleccionado
                if (opt == 'W') { // Monedero en la app
                    realizePayment(amountToPay);
                    server.registerPayment(journeyService.getServiceID(),user,amountToPay,opt);
                } else if (opt == 'C' || opt == 'D' || opt == 'B') { // Tarjeta, Débito o Bizum
                    // Lógica de conexión con otros servicios de pago, si corresponde
                    System.out.println("Procesando pago con método externo...");
                } else {
                    // Este bloque no debería ejecutarse, pero si llegamos aquí, lanzamos una excepción
                    throw new ProceduralException("Método de pago no reconocido. No se pudo procesar el pago.");
                }
            }
            // Operación interna para realizar el pago desde el monedero de la app
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

