package services.smartfeatures;

import exceptions.ConnectException;

public class SimulatedUnbondedBTSignal implements UnbondedBTSignal {

    private boolean isBluetoothAvailable;

    // Constructor que permite simular la disponibilidad de Bluetooth.
    public SimulatedUnbondedBTSignal(boolean isBluetoothAvailable) {
        this.isBluetoothAvailable = isBluetoothAvailable;
    }

    @Override
    public void BTbroadcast() throws ConnectException {
        if (!isBluetoothAvailable) {
            throw new ConnectException("Error en la conexión Bluetooth.");
        }

        // Simulamos la emisión del ID de la estación.
        System.out.println("Transmitiendo ID de estación por Bluetooth...");
    }
}
