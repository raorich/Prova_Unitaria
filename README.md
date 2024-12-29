# Proyecto Micromovilidad Compartida

## Descripción
Este proyecto simula un sistema de **micromovilidad compartida**, diseñado para gestionar vehículos compartidos, como bicicletas y scooters, mediante una aplicación móvil. Los usuarios pueden emparejarse con vehículos, iniciar trayectos, y administrar la disponibilidad y ubicación de los mismos.
## Característiques Principals
- **Gestió de Viatges:** Inici, finalització i càlcul de distàncies reals entre ubicacions.
- **Lectura de Codis QR:** Simulació de verificació de vehicles a través de fitxers QR locals.
- **Pagaments Virtuals:** Deducció automàtica de saldo amb validació de fons suficients.
- **Connexions Simulades:** Bluetooth i connexions al servidor amb probabilitats realistes d'error.
- **Gestió d'Errors:** Maneig d'excepcions i condicions específiques per garantir la integritat del sistema.

## Estructura del Projecte
- **`data`:** Models de dades com ubicacions, comptes d'usuari i identificadors.
- **`micromobility`:** Lògica principal de control de viatges i vehicles.
- **`payment`:** Gestió de moneder virtual i processament de pagaments.
- **`services`:** Simulació de servidor i comunicació entre components.
- **`QRDecoder`:** Implementació del procés de lectura de codis QR.

## Requisits del Sistema
- **Java 11 o superior**
- **IDE Recomanat:** IntelliJ IDEA, Eclipse o qualsevol IDE compatible amb Java.

## Instal·lació i Execució
1. Cloneu el repositori:
   ```bash
   git clone https://github.com/usuari/micromobility-system.git
