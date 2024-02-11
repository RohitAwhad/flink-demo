package schema;

public class Sensor {
    public String timestamp;
    public float temperature;
    public int sensor_id;

    @Override
    public String toString() {
        return "{\"timestamp\": "+timestamp+",\"temperature\": "+temperature+",\"sensor_id\": "+sensor_id+" }";
    }
}
