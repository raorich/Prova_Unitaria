import data.ServiceID;
import data.UserAccount;
import micromobility.JourneyService;
import exceptions.NotEnoughWalletException;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletPaymentTest {
    private JourneyService journeyService;
    private UserAccount userAccount;
    private Wallet wallet;
    private WalletPayment walletPayment;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount("user123");
        ServiceID serviceID = new ServiceID("S12345");

        journeyService = new JourneyService(serviceID, userAccount, new BigDecimal("100.00"), 'W');

        wallet = new Wallet(new BigDecimal("100.00"));

        walletPayment = new WalletPayment(journeyService, userAccount, new BigDecimal("50.00"), wallet);
    }

    @Test
    void testProcessPayment_Success() throws NotEnoughWalletException {
        walletPayment.processPayment();
        assertEquals(new BigDecimal("50.00"), wallet.getBalance());  // El saldo debe reducirse
    }

    @Test
    void testProcessPayment_InsufficientBalance() {
        walletPayment = new WalletPayment(journeyService, userAccount, new BigDecimal("200.00"), wallet);  //pagamos más de lo que tenemos
        assertThrows(NotEnoughWalletException.class, () -> walletPayment.processPayment());
    }
    @Test
    void testWalletPaymentConstructor_NullWallet() {
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, userAccount, new BigDecimal("50.00"), null));
    }
    @Test
    void testToString() {
        String expectedToString = "WalletPayment{" +
                "journeyService=" + journeyService +
                ", user=" + userAccount +
                ", amount=" + walletPayment.getAmount() +
                ", wallet=" + wallet +
                '}';
        assertEquals(expectedToString, walletPayment.toString());
    }

}