package com.example.demo;

import java.util.Random;

public class RainController {
    private final GardenSimulatorAPI gardenAPI;
    private final Random random;

    // Constructor
    public RainController(GardenSimulatorAPI gardenAPI) {
        this.gardenAPI = gardenAPI;
        this.random = new Random();
    }

    /**
     * Simulate specific rainfall amount.
     *
     * @param amount The amount of rainfall to simulate.
     */
    public void simulateRain(int amount) {
        if (amount < 0) {
            System.out.println("Error: Rainfall amount cannot be negative.");
            Logger.log(Logger.LogLevel.ERROR, amount + " is an invalid rainfall amount");
            return;
        }
        gardenAPI.rain(amount);
        System.out.println("Rainfall of " + amount + " units simulated.");
        Logger.log(Logger.LogLevel.INFO,"Simulated rainfall of " + amount + " units.");
    }

    /**
     * Simulate random rainfall within a given range.
     *
     * @param minAmount Minimum amount of rainfall.
     * @param maxAmount Maximum amount of rainfall.
     */
    public void simulateRandomRain(int minAmount, int maxAmount) {
        if (minAmount < 0 || maxAmount < 0 || minAmount > maxAmount) {
            System.out.println("Error: Invalid rainfall range.");
            Logger.log(Logger.LogLevel.ERROR, "Invalid rainfall range: " + minAmount + " to " + maxAmount);
            return;
        }
        int amount = random.nextInt(maxAmount - minAmount + 1) + minAmount;
        simulateRain(amount);
    }

    /**
     * Simulate light rainfall (1-5 units).
     */
    public void simulateLightRain() {
        int amount = random.nextInt(5) + 1;
        simulateRain(amount);
        System.out.println("Light rainfall simulated.");
    }

    /**
     * Simulate moderate rainfall (6-15 units).
     */
    public void simulateModerateRain() {
        int amount = random.nextInt(10) + 6;
        simulateRain(amount);
        System.out.println("Moderate rainfall simulated.");
    }

    /**
     * Simulate heavy rainfall (16-30 units).
     */
    public void simulateHeavyRain() {
        int amount = random.nextInt(15) + 16;
        simulateRain(amount);
        System.out.println("Heavy rainfall simulated.");
    }
}
