package com.example.demo;

import java.util.Random;

public class TemperatureController {
    private final GardenSimulatorAPI gardenAPI;
    private final Random random;

    // Constructor
    public TemperatureController(GardenSimulatorAPI gardenAPI) {
        this.gardenAPI = gardenAPI;
        this.random = new Random();
    }

    /**
     * Simulate a specific temperature change.
     *
     * @param temperature The temperature to set in the garden.
     */
    public void simulateTemperature(int temperature) {
        gardenAPI.temperature(temperature);
        System.out.println("Temperature set to " + temperature + " degrees.");
        Logger.log(Logger.LogLevel.INFO,"Temperature set to " + temperature + " degrees.");
    }

    /**
     * Simulate a random temperature within a given range.
     *
     * @param minTemperature Minimum temperature.
     * @param maxTemperature Maximum temperature.
     */
    public void simulateRandomTemperature(int minTemperature, int maxTemperature) {
        if (minTemperature > maxTemperature) {
            System.out.println("Error: Minimum temperature cannot be greater than maximum temperature.");
            Logger.log(Logger.LogLevel.ERROR, "Minimum temperature (" + minTemperature + ") should not be greater than maximum temperature (" + maxTemperature + ")");
            return;
        }

        int temperature = random.nextInt(maxTemperature - minTemperature + 1) + minTemperature;
        simulateTemperature(temperature);
    }

    /**
     * Simulate summer temperature.
     * Summer temperatures range from 80 to 100 degrees.
     */
    public void simulateSummer() {
        int temperature = random.nextInt(21) + 80; // Random temperature between 80 and 100
        simulateTemperature(temperature);
        System.out.println("Summer temperature simulated: " + temperature + " degrees.");
        Logger.log(Logger.LogLevel.INFO,"Summer temperature simulated with " + temperature + " degrees");
    }

    /**
     * Simulate winter temperature.
     * Winter temperatures range from 30 to 50 degrees.
     */
    public void simulateWinter() {
        int temperature = random.nextInt(21) + 30; // Random temperature between 30 and 50
        simulateTemperature(temperature);
        System.out.println("Winter temperature simulated: " + temperature + " degrees.");
        Logger.log(Logger.LogLevel.INFO,"Winter temperature simulated with " + temperature + " degrees");

    }

    /**
     * Simulate spring or fall temperature.
     * Temperatures range from 60 to 75 degrees.
     */
    public void simulateSpringOrFall() {
        int temperature = random.nextInt(16) + 60; // Random temperature between 60 and 75
        simulateTemperature(temperature);
        System.out.println("Spring/Fall temperature simulated: " + temperature + " degrees.");
        Logger.log(Logger.LogLevel.INFO,"Spring/fall temperature simulated with " + temperature + " degrees");
    }
}
