package micromobility;


import domain.JourneyService;
import exceptions.ConnectException;
import exceptions.NotEnoughWalletException;
import exceptions.ProceduralException;
import data.UserAccount;
import java.math.BigDecimal;

public class Payment {

    private JourneyService journeyService; // Asociación con el servicio
    private UserAcc user; // Asociación con el usuario
    private char paymentMethod; // Método de pago seleccionado
    private BigDecimal amount; // Importe del pago

    public Payment(JourneyService journeyService, UserAcc user) {
        if (journeyService == null || user == null) {
            throw new IllegalArgumentException("JourneyService y UserAcc no pueden ser nulos.");
        }
        this.journeyService = journeyService;
        this.user = user;
    }

    /**
     * Seleccionar el método de pago y procesar el importe.
     * 
     * @param opt El método de pago seleccionado ('C', 'D', 'W', 'B').
     * @throws ProceduralException Si el vehículo no está desvinculado o el método es inválido.
     * @throws NotEnoughWalletException Si el saldo del monedero es insuficiente.
     * @throws ConnectException Si hay problemas de conexión con el servidor o servicio de pago.
     */
    public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
        // Precondición: Verificar que el vehículo esté desvinculado
        if (!journeyService.isVehicleUnbonded()) {
            throw new ProceduralException("El vehículo aún está vinculado. Desvincúlelo antes de proceder al pago.");
        }

        // Validar el método de pago
        if (opt != 'C' && opt != 'D' && opt != 'W' && opt != 'B') {
            throw new ProceduralException("Método de pago no válido.");
        }
        this.paymentMethod = opt;

        // Obtener el importe a pagar
        this.amount = journeyService.getImport();

        // Procesar el pago según el método seleccionado
        try {
            switch (paymentMethod) {
                case 'C':
                case 'D':
                    processCardPayment();
                    break;
                case 'W':
                    processWalletPayment();
                    break;
                case 'B':
                    processBizumPayment();
                    break;
                default:
                    throw new ProceduralException("Método de pago desconocido.");
            }

            // Salida: Detalles del pago
            System.out.println("Pago efectuado con éxito:");
            System.out.println("Método: " + getPaymentMethodName());
            System.out.println("Importe: " + amount);

        } catch (NotEnoughWalletException | ConnectException e) {
            throw e; // Propagar excepciones específicas
        } catch (Exception e) {
            throw new ProceduralException("Error inesperado al procesar el pago.");
        }
    }

    /**
     * Procesa el pago con tarjeta (crédito/débito).
     * 
     * @throws ConnectException Si hay problemas de conexión con el servicio de pago.
     */
    private void processCardPayment() throws ConnectException {
        System.out.println("Conectando con la pasarela de pago...");
        if (!journeyService.isServerAvailable()) {
            throw new ConnectException("Error de conexión con la pasarela de pago.");
        }
        System.out.println("Pago con tarjeta procesado con éxito.");
    }

    /**
     * Procesa el pago mediante monedero.
     * 
     * @throws NotEnoughWalletException Si el saldo del monedero es insuficiente.
     */
    private void processWalletPayment() throws NotEnoughWalletException {
        if (user.getWalletBalance().compareTo(amount) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente para realizar el pago con monedero.");
        }
        user.deductFromWallet(amount);
        System.out.println("Pago realizado con éxito desde el monedero. Importe: " + amount);
    }

    /**
     * Procesa el pago mediante Bizum.
     * 
     * @throws ConnectException Si hay problemas de conexión con el servicio de pago.
     */
    private void processBizumPayment() throws ConnectException {
        System.out.println("Conectando con el servicio de Bizum...");
        if (!journeyService.isServerAvailable()) {
            throw new ConnectException("Error de conexión con el servicio de Bizum.");
        }
        System.out.println("Pago con Bizum procesado con éxito.");
    }

    /**
     * Obtiene el nombre del método de pago.
     * 
     * @return El nombre del método de pago.
     */
    private String getPaymentMethodName() {
        switch (paymentMethod) {
            case 'C': return "Tarjeta de Crédito";
            case 'D': return "Tarjeta de Débito";
            case 'W': return "Monedero";
            case 'B': return "Bizum";
            default: return "Desconocido";
        }
    }

    // Métodos de utilidad
    public JourneyService getJourneyService() {
        return journeyService;
    }

    public UserAcc getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public char getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "journeyService=" + journeyService +
                ", user=" + user +
                ", paymentMethod=" + getPaymentMethodName() +
                ", amount=" + amount +
                '}';
    }
}
