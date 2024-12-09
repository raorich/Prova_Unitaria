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
}
