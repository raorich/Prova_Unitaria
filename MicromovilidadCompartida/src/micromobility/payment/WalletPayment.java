package micromobility;
package payment;

import domain.UserAcc;
import services.JourneyService;
import exceptions.*;
import wallet.Wallet;

import java.math.BigDecimal;

public class WalletPayment extends Payment {

    private Wallet wallet; // Asociación con la clase Wallet

    /**
     * Constructor de WalletPayment
     * 
     * @param journeyService El servicio asociado al pago.
     * @param user El usuario que realiza el pago.
     * @param wallet El monedero asociado al usuario.
     */
    public WalletPayment(JourneyService journeyService, UserAcc user, Wallet wallet) {
        super(journeyService, user);
        if (wallet == null) {
            throw new IllegalArgumentException("El monedero no puede ser nulo.");
        }
        this.wallet = wallet;
    }

    /**
     * Procesa el pago utilizando el monedero.
     * 
     * @param amount El importe del pago.
     * @throws NotEnoughWalletException Si el saldo del monedero es insuficiente.
     */
    @Override
    public void processPayment(BigDecimal amount) throws NotEnoughWalletException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo.");
        }

        // Verificar saldo suficiente
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
        }

        // Deducir el saldo del monedero
        wallet.deduct(amount);

        // Asociar el importe al pago
        super.setAmount(amount);

        System.out.println("Pago realizado con éxito desde el monedero. Importe: " + amount);
    }

    /**
     * Obtiene el monedero asociado al pago.
     * 
     * @return El monedero del usuario.
     */
    public Wallet getWallet() {
        return wallet;
    }
}
