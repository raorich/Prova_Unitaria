package micromobility;
package payment;

import exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class Wallet {

    private BigDecimal balance; // Saldo almacenado en el monedero

    /**
     * Constructor para inicializar el saldo del monedero.
     *
     * @param initialBalance El saldo inicial del monedero.
     */
    public Wallet(BigDecimal initialBalance) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser no negativo.");
        }
        this.balance = initialBalance;
    }

    /**
     * Obtiene el saldo actual del monedero.
     *
     * @return El saldo actual.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Método privado para deducir un importe del saldo del monedero.
     *
     * @param imp El importe a deducir.
     * @throws NotEnoughWalletException Si el saldo es insuficiente.
     */
    private void deduct(BigDecimal imp) throws NotEnoughWalletException {
        if (imp == null || imp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe a deducir debe ser positivo.");
        }

        if (balance.compareTo(imp) < 0) {
            throw new NotEnoughWalletException("Saldo insuficiente en el monedero.");
        }

        // Restar el importe del saldo actual
        balance = balance.subtract(imp);
    }

    /**
     * Aplica una deducción pública utilizando el método interno `deduct`.
     *
     * @param imp El importe a deducir.
     * @throws NotEnoughWalletException Si el saldo es insuficiente.
     */
    public void applyDeduction(BigDecimal imp) throws NotEnoughWalletException {
        deduct(imp);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + balance +
                '}';
    }
}
