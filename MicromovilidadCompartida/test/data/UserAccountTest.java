package data;

import data.UserAccount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

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
        UserAccount account1 = new UserAccount("user123");
        UserAccount account2 = new UserAccount("user123");
        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void testToString() {
        UserAccount account = new UserAccount("user123");
        assertEquals("UserAccount{accountId='user123'}", account.toString());
    }
    @Test
    void testEquals_SameObject() {
        UserAccount account = new UserAccount("User123");
        assertTrue(account.equals(account)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        UserAccount account = new UserAccount("User123");
        assertFalse(account.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        UserAccount account = new UserAccount("User123");
        String notAUserAccount = "NotAUserAccount";
        assertFalse(account.equals(notAUserAccount)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentAccountId() {
        UserAccount account1 = new UserAccount("User123");
        UserAccount account2 = new UserAccount("User456");
        assertFalse(account1.equals(account2)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        UserAccount account1 = new UserAccount("User123");
        UserAccount account2 = new UserAccount("User123");
        assertTrue(account1.equals(account2)); // Mismos IDs, deben ser iguales
    }
}
