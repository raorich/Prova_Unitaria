package data;

public final class GeographicPoint {
    private final float latitude;
    private final float longitude;

    public GeographicPoint(float latitude, float longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be in range [-90, 90]");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be in range [-180, 180]");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals (Object o) {
        boolean eq;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeographicPoint gP = (GeographicPoint) o;
        eq = ((latitude == gP.latitude) && (longitude == gP.longitude));
        return eq;
    }


    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(latitude);
        result = prime * result + Float.floatToIntBits(longitude);
        return result;
    }

    @Override
    public String toString() {
        return "GeographicPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
