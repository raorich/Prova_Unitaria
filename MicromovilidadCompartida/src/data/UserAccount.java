package data;

public final class UserAccount {
    private final String accountId;

    public UserAccount(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("UserAccount ID cannot be null or empty");
        }
        if (!accountId.matches("[a-zA-Z0-9]{3,20}")) {
            throw new IllegalArgumentException("UserAccount ID must be alphanumeric and 3-20 characters long");
        } //una de propia
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return accountId.hashCode();
    }

    @Override
    public String toString() {
        return "UserAccount{" + "accountId='" + accountId + '\'' + '}';
    }
}
