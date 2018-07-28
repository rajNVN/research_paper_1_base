package util.preprocessing;

public class Object {
    private double latitude;
    private double longitude;
    private String mCode;
    private String name;
    private String type;

    public Object(double latitude, double longitude, String mCode, String name, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mCode = mCode;
        this.name = name;
        this.type = type;
    }

    public String getMCode() {
        return mCode;
    }

    public void print() {
        System.out.println(latitude + " , " + longitude + " , " + name + " , " + type);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
