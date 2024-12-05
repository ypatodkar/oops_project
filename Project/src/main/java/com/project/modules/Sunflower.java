package com.project.modules;

import com.project.factory.PlantType;
import com.project.logger.Logger;

import java.util.List;

public class Sunflower extends Bush {
    private int seedProduction;
    private static final String emoji = "🌻";
    public static String getEmoji() {
        return emoji;
    }

    public Sunflower(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, int initialDensity, int trimmingFrequency, int seedProduction) {
        super(PlantType.SUNFLOWER, name, waterRequirement, pestVulnerabilities, tempLow, tempHigh, initialDensity, trimmingFrequency);
        this.seedProduction = seedProduction;
    }

    // Getter and Setter for seedProduction
    public int getSeedProduction() {
        return seedProduction;
    }

    public void setSeedProduction(int seedProduction) {
        this.seedProduction = seedProduction;
        Logger.log(Logger.LogLevel.INFO, getName() + " seed production set to " + seedProduction + ".");
    }

    @Override
    public void grow() {
        super.grow();
        // Additional Sunflower-specific growth logic
        seedProduction += 1;
        Logger.log(Logger.LogLevel.INFO, getName() + " is producing seeds. Total seeds: " + seedProduction + ".");
    }

    @Override
    public void dailyCheck() {
        super.dailyCheck();
        // Additional Sunflower-specific daily checks
        Logger.log(Logger.LogLevel.INFO, "Checking seed production for " + getName() + ".");
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println(getName() + " requires specific care: Regular watering, adequate sunlight, trimming for seed production, and pest management.");
    }
}
