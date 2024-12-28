package micromobility.payment;

import data.UserAccount;
import domain.JourneyService;
import exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class WalletPayment extends Payment {

    private Wallet wallet; // Asociación con el monedero

    public WalletPayment(JourneyService journeyService, UserAccount user, BigDecimal amount, Wallet wallet) {
        super(journeyService, user, amount);
        this.wallet = wallet;
    }

    @Override
    public void processPayment() throws NotEnoughWalletException {
        // Verificamos si el monedero tiene suficiente saldo
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
        }

        // Llamamos al método público applyDeduction en lugar de deducir directamente
        wallet.applyDeduction(amount);
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
