package com.example.demo;

public class HeatingController {
    private final GardenSimulatorAPI gardenAPI;
    private int heatingThreshold; // Minimum temperature below which heating is activated
    private int heatingIncrement; // Amount by which temperature is increased during heating

    // Constructor
    public HeatingController(GardenSimulatorAPI gardenAPI, int heatingThreshold, int heatingIncrement) {
        this.gardenAPI = gardenAPI;
        this.heatingThreshold = heatingThreshold;
        this.heatingIncrement = heatingIncrement;
    }

    // Getters and Setters for flexibility
    public int getHeatingThreshold() {
        return heatingThreshold;
    }

    public void setHeatingThreshold(int heatingThreshold) {
        this.heatingThreshold = heatingThreshold;
    }

    public int getHeatingIncrement() {
        return heatingIncrement;
    }

    public void setHeatingIncrement(int heatingIncrement) {
        this.heatingIncrement = heatingIncrement;
    }

    /**
     * Simulate heating in the garden when the temperature is below the threshold.
     *
     * @param currentTemperature The current temperature of the garden.
     */
    public void applyHeating(int currentTemperature) {
        if (currentTemperature < heatingThreshold) {
            int newTemperature = currentTemperature + heatingIncrement;
            gardenAPI.temperature(newTemperature);
            System.out.println("Heating applied. Temperature raised to " + newTemperature + " degrees.");
            Logger.log(Logger.LogLevel.INFO,"Heating applied: Raised temperature from " + currentTemperature + " to " + newTemperature);
        } else {
            System.out.println("No heating needed. Current temperature is " + currentTemperature + " degrees.");
            Logger.log(Logger.LogLevel.INFO,"No heating applied: Current temperature is " + currentTemperature);
        }
    }
}
