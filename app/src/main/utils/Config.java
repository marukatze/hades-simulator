package main.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("⚠️ config.properties not found, using defaults");
                setDefaults();
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setDefaults();
        }
    }

    private static void setDefaults() {
        props.setProperty("buffer.capacity", "4");
        props.setProperty("charon.count", "4");
        props.setProperty("charon.mu", "2.5");
        props.setProperty("source.count", "3");
        props.setProperty("simulation.maxTime", "1000.0");
    }

    public static int getBufferCapacity() {
        return Integer.parseInt(props.getProperty("buffer.capacity", "4"));
    }

    public static int getCharonCount() {
        return Integer.parseInt(props.getProperty("charon.count", "4"));
    }

    public static double getCharonLambda() {
        return Double.parseDouble(props.getProperty("charon.lambda", "2.5"));
    }

    public static int getSourceCount() {
        return Integer.parseInt(props.getProperty("source.count", "3"));
    }

    public static double getSourceMin(int sourceId) {
        return Double.parseDouble(props.getProperty("source." + sourceId + ".min", "0.2"));
    }

    public static double getSourceMax(int sourceId) {
        return Double.parseDouble(props.getProperty("source." + sourceId + ".max", "0.3"));
    }

    public static int getSourcePriority(int sourceId) {
        return Integer.parseInt(props.getProperty("source." + sourceId + ".priority", String.valueOf(sourceId)));
    }

    public static double getSimulationMaxTime() {
        return Double.parseDouble(props.getProperty("simulation.maxTime", "1000.0"));
    }

    public static String getMode() {
        return props.getProperty("simulation.mode", "step");
    }
}