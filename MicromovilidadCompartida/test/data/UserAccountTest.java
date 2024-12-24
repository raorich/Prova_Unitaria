package data;

import data.UserAccount;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    private static UserAccount validAccount1;
    private static UserAccount validAccount2;
    private static UserAccount differentAccount;

    @BeforeAll
    static void setUp() {
        validAccount1 = new UserAccount("user123");
        validAccount2 = new UserAccount("user123");
        differentAccount = new UserAccount("User456");
    }

    @Test
    void testValidUserAccount() {
        UserAccount account = new UserAccount("user123");
        assertEquals("user123", account.getAccountId());
    }

    @Test
    void testNullOrEmptyUserAccount() {
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(null));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(""));
    }

    @Test
    void testInvalidUserAccount() {
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("12"));  // Too short
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("ThisIsAReallyLongAccountID123456"));  // Too long
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("User@123"));  // Invalid character
    }

    @Test
    void testEqualsAndHashCode() {
        assertEquals(validAccount1, validAccount2);
        assertEquals(validAccount1.hashCode(), validAccount2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("UserAccount{accountId='user123'}", validAccount1.toString());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(validAccount1.equals(validAccount1)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validAccount1.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        String notAUserAccount = "NotAUserAccount";
        assertFalse(validAccount1.equals(notAUserAccount)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentAccountId() {
        assertFalse(validAccount1.equals(differentAccount)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        assertTrue(validAccount1.equals(validAccount2)); // Mismos IDs, deben ser iguales
    }
}
