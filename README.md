# Micromovilidad Compartida - Proyecto de Ingeniería de Software
Descripción
Este proyecto simula un sistema de micromovilidad compartida, donde los usuarios pueden interactuar con vehículos (bicicletas, scooters) a través de una aplicación móvil. El sistema está diseñado para gestionar vehículos, emparejarlos con cuentas de usuario, realizar trayectos y administrar los estados de los vehículos.

Estructura del Proyecto
Paquete data: Contiene las clases para representar entidades clave como UserAccount, VehicleID, StationID, etc.
Paquete services: Define los servicios necesarios para la funcionalidad del sistema, como Server, UnbondedBTSignal, QRDecoder, etc.
Paquete micromobility: Contiene las clases PMVehicle y JourneyService, que gestionan el estado del vehículo y el trayecto.
Paquete handler: La clase JourneyRealizeHandler actúa como controladora de los eventos del caso de uso "Realizar desplazamiento".
Funcionalidades
Emparejamiento y desemparejamiento de vehículos.
Gestión del trayecto: Iniciar, detener y calcular valores de distancia, velocidad y costo.
Interacción con Bluetooth para la comunicación entre la app y los vehículos.
Requisitos
Java 11 o superior
JUnit 5 para pruebas unitarias
Instalación
Clona este repositorio.

Compila el proyecto usando Maven o Gradle.

Ejecuta las pruebas con el comando:

bash
Копировать код
mvn test
Pruebas
Se han implementado pruebas unitarias para verificar el comportamiento de las clases JourneyRealizeHandler, PMVehicle y JourneyService utilizando JUnit 5.
