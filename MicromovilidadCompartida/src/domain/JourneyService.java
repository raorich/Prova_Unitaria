package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalTime;

import data.*;

public class JourneyService {
    private LocalDateTime initDate;  // Fecha y hora de inicio
    private LocalDateTime endDate;   // Fecha y hora de fin
    private LocalDateTime endHour;   //
    private boolean inProgress;      // Estado del trayecto (en progreso o no)
    private float distance;          // Distancia recorrida en metros
    private int duration;            // Duración del trayecto en minutos
    private float avgSpeed;          // Velocidad promedio en km/h
    private BigDecimal importAmount; // Importe a pagar por el servicio

    // Nuevos atributos
    private StationID orgStatID;    // Estación de origen
    private GeographicPoint originPoint;  // Ubicación del conductor
    private GeographicPoint endPoint;  // Ubicación del conductor
    private UserAccount userAccount;     // Cuenta del usuario
    private VehicleID vehicleID;    // ID del vehículo

    // Constructor
    public JourneyService() {
        this.inProgress = false;
    }

    // Setters y Getters

    // Inicialización del trayecto
    public void setServiceInit(LocalDateTime initDate) {
        this.initDate = initDate;
        this.inProgress = true;  // Marcar trayecto como en progreso
    }

    public LocalDateTime getServiceInit() {
        return this.initDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    // Métodos para distancia, duración, velocidad promedio e importe
    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setImportAmount(BigDecimal importAmount) {
        this.importAmount = importAmount;
    }

    public BigDecimal getImportAmount() {
        return importAmount;
    }

    // Métodos para los atributos adicionales
    public void setOrgStatID(StationID orgStatID) {
        this.orgStatID = orgStatID;
    }

    public StationID getOrgStatID() {
        return orgStatID;
    }

    public void setOriginPoint(GeographicPoint originPoint) {
        this.originPoint = originPoint;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setVehicleID(VehicleID vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleID getVehicleID() {
        return vehicleID;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }


    public void setEndPoint(GeographicPoint destination) {
        this.endPoint=endPoint;
    }

    public void setEndDate(LocalDate toLocalDate) {
        this.endDate= toLocalDate.atStartOfDay();
    }

    public void setEndHour(LocalTime toLocalTime) {
        this.endHour= LocalDateTime.from(toLocalTime);
    }
}
