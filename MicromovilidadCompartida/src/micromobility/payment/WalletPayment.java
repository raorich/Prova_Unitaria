package micromobility.payment;

import data.UserAccount;
import micromobility.JourneyService;
import exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class WalletPayment extends Payment {

    private Wallet wallet;

    public WalletPayment(JourneyService journeyService, UserAccount user, BigDecimal amount, Wallet wallet) {
        super(journeyService, user, amount);
        if (wallet == null) {
            throw new IllegalArgumentException("El monedero no puede ser nulo.");
        }
        this.wallet = wallet;
    }

    public void processPayment() throws NotEnoughWalletException {
        // Verificamos si el monedero tiene suficiente saldo
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
        }

        // Deduct the amount from the wallet balance
        wallet.applyDeduction(amount);

        // Update the JourneyService after the payment
        journeyService.setImportAmount(journeyService.getImportAmount().subtract(amount));  // Actualiza el importe restante

        System.out.println("Pago realizado con éxito usando el monedero. Importe: " + amount);
    }

    @Override
    public String toString() {
        return "WalletPayment{" +
                "journeyService=" + journeyService +
                ", user=" + user +
                ", amount=" + amount +
                ", wallet=" + wallet +
                '}';
    }

}
