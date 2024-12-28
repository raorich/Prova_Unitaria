package micromobility.payment;

import exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class Wallet {

    private BigDecimal balance; // Saldo almacenado en el monedero

    public Wallet(BigDecimal initialBalance) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser no negativo.");
        }
        this.balance = initialBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    // Método público para aplicar la deducción
    public void applyDeduction(BigDecimal imp) throws NotEnoughWalletException {
        deduct(imp); // Llama al método privado deduct
    }

    // Método privado para deducir un importe del saldo del monedero
    private void deduct(BigDecimal imp) throws NotEnoughWalletException {
        if (imp == null || imp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe a deducir debe ser positivo.");
        }

        if (balance.compareTo(imp) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
        }

        balance = balance.subtract(imp);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + balance +
                '}';
    }
}
