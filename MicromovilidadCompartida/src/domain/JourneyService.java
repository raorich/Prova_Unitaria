package domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import data.*;

public class JourneyService {
    private LocalDateTime initDate;
    private LocalDateTime endDate;
    private boolean inProgress;
    private float distance;
    private int duration;
    private float avgSpeed;
    private BigDecimal importAmount;

    // Nuevos atributos
    private StationID orgStatID;  // Estación de origen
    private GeographicPoint originPoint;  // Ubicación del conductor
    private UserAccount userAccount;  // Cuenta de usuario
    private VehicleID vehicleID;  // ID del vehículo

    public JourneyService() {
        this.inProgress = false;
    }

    // Setters existentes
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

    // Nuevos setters para los atributos adicionales
    public void setOrgStatID(StationID orgStatID) {
        this.orgStatID = orgStatID;
    }

    public void setOriginPoint(GeographicPoint originPoint) {
        this.originPoint = originPoint;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setVehicleID(VehicleID vehicleID) {
        this.vehicleID = vehicleID;
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

    // Nuevos getters para los atributos adicionales
    public StationID getOrgStatID() {
        return orgStatID;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public VehicleID getVehicleID() {
        return vehicleID;
    }
}
