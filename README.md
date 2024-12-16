# Proyecto Micromovilidad Compartida

## Descripción
Este proyecto simula un sistema de **micromovilidad compartida**, diseñado para gestionar vehículos compartidos, como bicicletas y scooters, mediante una aplicación móvil. Los usuarios pueden emparejarse con vehículos, iniciar trayectos, y administrar la disponibilidad y ubicación de los mismos.

## Requisitos
- **Java 11+**
- **JUnit 5** para pruebas unitarias
- **Maven** para la construcción del proyecto

## Estructura del Proyecto
- **`data`**: Contiene las clases para representar las entidades clave del sistema como `UserAccount`, `VehicleID`, `StationID`.
- **`services`**: Define los servicios necesarios para la funcionalidad del sistema como `Server`, `UnbondedBTSignal`, `QRDecoder`.
- **`micromobility`**: Contiene las clases `PMVehicle` y `JourneyService` para la gestión de vehículos y trayectos.
- **`handler`**: Contiene la clase `JourneyRealizeHandler`, responsable de manejar los eventos del caso de uso "Realizar desplazamiento".

## Funcionalidades
- **Emparejamiento de vehículos**: Los usuarios pueden emparejarse con un vehículo a través de un código QR.
- **Gestión de trayectos**: Iniciar y detener trayectos, calcular distancia, duración y velocidad.
- **Cambio de estado de vehículos**: Los vehículos pueden pasar entre los estados "Disponible", "En uso" y "No disponible".
  
## Instalación
1. Clona este repositorio:
   ```bash
   git clone <repo_url>
