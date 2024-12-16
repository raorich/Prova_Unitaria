package domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class JourneyService {
    private LocalDateTime initDate;
    private LocalDateTime endDate;
    private boolean inProgress;
    private float distance;
    private int duration;
    private float avgSpeed;
    private BigDecimal importAmount;

    public JourneyService() {
        this.inProgress = false;
    }

    // Setters
    public void setServiceInit(LocalDateTime initDate) {
        this.initDate = initDate;
        this.inProgress = true;
    }

    public void setServiceFinish(LocalDateTime endDate, float distance, int duration, float avgSpeed, BigDecimal importAmount) {
        this.endDate = endDate;
        this.distance = distance;
        this.duration = duration;
        this.avgSpeed = avgSpeed;
        this.importAmount = importAmount;
        this.inProgress = false;
    }

    // Getters
    public boolean isInProgress() {
        return inProgress;
    }

    public LocalDateTime getInitDate() {
        return initDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public float getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public BigDecimal getImportAmount() {
        return importAmount;
    }
}
