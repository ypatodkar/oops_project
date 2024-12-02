
package com.project.modules;

import com.project.logger.Logger;

import java.util.List;

public abstract class Plant { // Abstract class to prevent direct instantiation
    private String name;
    private int waterRequirement;
    private int currentWaterLevel;
    private List<String> pestVulnerabilities;
    private int temperatureToleranceLow;
    private int temperatureToleranceHigh;
    private boolean isAlive;

    public Plant(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh) {
        this.name = name;
        this.waterRequirement = waterRequirement;
        this.currentWaterLevel = 0;
        this.pestVulnerabilities = pestVulnerabilities;
        this.temperatureToleranceLow = tempLow;
        this.temperatureToleranceHigh = tempHigh;
        this.isAlive = true;
    }

    // Getters for necessary fields
    public String getName() {
        return name;
    }

    public int getCurrentWaterLevel() {
        return currentWaterLevel;
    }


    public int getWaterRequirement() {
        return waterRequirement;
    }

    public List<String> getPestVulnerabilities() {
        return pestVulnerabilities;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void water(int amount) {
        if (!isAlive) return;

        currentWaterLevel += amount;
        Logger.log(Logger.LogLevel.INFO, name + " watered with " + amount + " units.");

        if (currentWaterLevel > waterRequirement * 2) {
            Logger.log(Logger.LogLevel.WARNING, name + " has been overwatered and died.");
            System.out.println(name + " has been overwatered and died.");
            isAlive = false;
        }
    }

    public void setPestVulnerabilities(List<String> pestVulnerabilities) {
        this.pestVulnerabilities = pestVulnerabilities;
    }


    public void adjustTemperature(int temperature) {
        if (!isAlive) return;

        Logger.log(Logger.LogLevel.INFO, "Adjusting temperature for " + name + " to " + temperature + " degrees.");
        if (temperature < temperatureToleranceLow || temperature > temperatureToleranceHigh) {
            Logger.log(Logger.LogLevel.WARNING, name + " could not tolerate the temperature and died.");
            System.out.println(name + " could not tolerate the temperature and died.");
            isAlive = false;
        }
    }

    public void pestAttack(String pest) {
        if (!isAlive) return;

        Logger.log(Logger.LogLevel.INFO, "Simulating pest attack on " + name + " by " + pest + ".");
        if (pestVulnerabilities.contains(pest)) {
            Logger.log(Logger.LogLevel.WARNING, name + " was attacked by " + pest + " and died.");
            System.out.println(name + " was attacked by " + pest + " and died.");
            isAlive = false;
        }
    }



    public abstract void grow();
    public abstract void dailyCheck();

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
    // Default implementation for special care instructions
    public void displaySpecialCareInstructions() {
        System.out.println(name + " requires general care: Water as needed and maintain a suitable temperature.");
    }
}
