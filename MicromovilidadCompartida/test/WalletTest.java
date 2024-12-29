import exceptions.*;
import micromobility.payment.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
        private Wallet wallet;

        @BeforeEach
        void setUp() {
            wallet = new Wallet(new BigDecimal("100.00"));
        }
        @Test
        void testWalletConstructor_Valid() {
            assertNotNull(wallet);
            assertEquals(new BigDecimal("100.00"), wallet.getBalance());  // Verificamos que el saldo inicial es correcto
        }
        @Test
        void testWalletConstructor_Invalid() {
            assertThrows(IllegalArgumentException.class, () -> new Wallet(null));  // El saldo no puede ser nulo
            assertThrows(IllegalArgumentException.class, () -> new Wallet(new BigDecimal("-10.00")));  // El saldo no puede ser negativo
        }
        @Test
        void testApplyDeduction_Valid() throws NotEnoughWalletException {
            wallet.applyDeduction(new BigDecimal("30.00"));
            assertEquals(new BigDecimal("70.00"), wallet.getBalance());  // El saldo debería haberse actualizado
        }
        @Test
        void testApplyDeduction_InsufficientFunds() {
            assertThrows(NotEnoughWalletException.class, () -> wallet.applyDeduction(new BigDecimal("200.00")));  // El saldo es insuficiente para deducir 200.00
        }
        @Test
        void testApplyDeduction_NegativeAmount() {
            assertThrows(IllegalArgumentException.class, () -> wallet.applyDeduction(new BigDecimal("-10.00")));  // El importe no puede ser negativo
        }
        @Test
        void testApplyDeduction_NullAmount() {
            assertThrows(IllegalArgumentException.class, () -> wallet.applyDeduction(null));  // El importe no puede ser nulo
        }

        @Test
        void testToString() {
            String expectedToString = "Wallet{balance=" + wallet.getBalance() + "}";
            assertEquals(expectedToString, wallet.toString());
        }
}