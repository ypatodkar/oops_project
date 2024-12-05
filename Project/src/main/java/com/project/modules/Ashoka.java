package com.project.modules;

import com.project.factory.PlantType;
import com.project.logger.Logger;

import java.util.List;

public class Ashoka extends Tree {
    private String floweringSeason;
    private static final String emoji = "🎄";
    public Ashoka(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, int initialHeight, int growthRate, String floweringSeason) {
        super(PlantType.ASHOKA, name, waterRequirement, pestVulnerabilities, tempLow, tempHigh, initialHeight, growthRate);
        this.floweringSeason = floweringSeason;
    }

    public static String getEmoji() {
        return emoji;
    }

    // Getter and Setter for floweringSeason
    public String getFloweringSeason() {
        return floweringSeason;
    }

    public void setFloweringSeason(String floweringSeason) {
        this.floweringSeason = floweringSeason;
        Logger.log(Logger.LogLevel.INFO, getName() + " flowering season set to " + floweringSeason + ".");
    }

    @Override
    public void grow() {
        super.grow();
        // Additional Ashoka-specific growth logic
        Logger.log(Logger.LogLevel.INFO, getName() + " is growing with flowering season: " + floweringSeason + ".");
    }

    @Override
    public void dailyCheck() {
        super.dailyCheck();
        // Additional Ashoka-specific daily checks
        Logger.log(Logger.LogLevel.INFO, "Checking flowering season for " + getName() + ".");
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println(getName() + " requires specific care: Regular watering, pruning for flowering, monitoring for pests, and ensuring optimal temperature.");
    }
}
