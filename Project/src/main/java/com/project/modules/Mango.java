package com.project.modules;

import com.project.factory.PlantType;
import com.project.logger.Logger;

import java.util.List;

/**
 * Represents a Mango plant.
 */
public class Mango extends Tree {
    private int fruitYield; // Number of mangoes per season

    public Mango(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, int initialHeight, int growthRate, int fruitYield) {
        super(PlantType.MANGO, name, waterRequirement, pestVulnerabilities, tempLow, tempHigh, initialHeight, growthRate);
        this.fruitYield = fruitYield;
    }

    // Getter and Setter for fruitYield
    public int getFruitYield() {
        return fruitYield;
    }

    public void setFruitYield(int fruitYield) {
        this.fruitYield = fruitYield;
        Logger.log(Logger.LogLevel.INFO, getName() + " fruit yield set to " + fruitYield + " mangoes per season.");
    }

    @Override
    public void grow() {
        super.grow();
        // Additional Mango-specific growth logic
        fruitYield += 5; // Example: Each growth cycle increases yield
        Logger.log(Logger.LogLevel.INFO, getName() + " is producing mangoes. Total yield: " + fruitYield + " mangoes.");
    }

    @Override
    public void dailyCheck() {
        super.dailyCheck();
        // Additional Mango-specific daily checks
        Logger.log(Logger.LogLevel.INFO, "Checking fruit yield for " + getName() + ".");
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println(getName() + " requires specific care: Regular watering, pruning for fruit production, monitoring for pests, and ensuring optimal temperature.");
    }
}
