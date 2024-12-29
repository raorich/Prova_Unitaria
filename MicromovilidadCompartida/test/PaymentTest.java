import data.*;
import micromobility.JourneyService;
import micromobility.payment.Payment;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private JourneyService journeyService;
    private UserAccount userAccount;
    private BigDecimal amount;
    private Wallet wallet;
    private Payment payment;

    @BeforeEach
    void setUp() {
        ServiceID serviceID =new ServiceID();
        userAccount = new UserAccount("user123");
        journeyService = new JourneyService(serviceID, userAccount, new BigDecimal("50.00"), 'W');
        amount = new BigDecimal("50.00");
        wallet = new Wallet(new BigDecimal("100.00"));

        // Crear un pago con monedero
        payment = new WalletPayment(journeyService, userAccount, amount, wallet);
    }

    @Test
    void testPaymentConstructor_NullJourneyService() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment(null, new UserAccount("user123"), new BigDecimal("50.00"))
        );
    }

    @Test
    void testPaymentConstructor_NullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment(new JourneyService(new ServiceID(), new UserAccount("user123"), new BigDecimal("50.00"), 'W'), null, new BigDecimal("50.00"))
        );
    }

    @Test
    void testPaymentConstructor_ValidParams() {
        assertNotNull(payment);
        assertEquals(journeyService, payment.getJourneyService());
        assertEquals(userAccount, payment.getUser());
        assertEquals(amount, payment.getAmount());
    }

    @Test
    void testGetJourneyService() {
        assertEquals(journeyService, payment.getJourneyService());
    }

    @Test
    void testGetUser() {
        assertEquals(userAccount, payment.getUser());
    }

    @Test
    void testGetAmount() {
        assertEquals(amount, payment.getAmount());
    }

    @Test
    void testToString() {
        String expectedToString = "WalletPayment{" +
                "journeyService=" + journeyService +
                ", user=" + userAccount +
                ", amount=" + amount +
                ", wallet=" + wallet +
                '}';
        assertEquals(expectedToString, payment.toString());
    }

}