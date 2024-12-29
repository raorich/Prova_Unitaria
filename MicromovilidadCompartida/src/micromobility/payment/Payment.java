package micromobility.payment;

import micromobility.JourneyService;
import data.UserAccount;

import java.math.BigDecimal;

public class Payment {

    protected JourneyService journeyService;
    protected UserAccount user;
    protected BigDecimal amount;

    public Payment(JourneyService journeyService, UserAccount user, BigDecimal amount) {
        if (journeyService == null || user == null) {
            throw new IllegalArgumentException("JourneyService y UserAccount no pueden ser nulos.");
        }
        this.journeyService = journeyService;
        this.user = user;
        this.amount = amount;
    }

    public JourneyService getJourneyService() {
        return journeyService;
    }
    public UserAccount getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "journeyService=" + journeyService +
                ", user=" + user +
                ", amount=" + amount +
                '}';
    }
}
