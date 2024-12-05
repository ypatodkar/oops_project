package com.project.modules;

import com.project.factory.PlantType;
import com.project.logger.Logger;

import java.util.List;

public class Tulsi extends Bush {
    private String medicinalProperties;
    private static final String emoji = "🍀";

    public static String getEmoji() {
        return emoji;
    }

    public Tulsi(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, int initialDensity, int trimmingFrequency, String medicinalProperties) {
        super(PlantType.GANJA, name, waterRequirement, pestVulnerabilities, tempLow, tempHigh, initialDensity, trimmingFrequency);
        this.medicinalProperties = medicinalProperties;
    }

    // Getter and Setter for medicinalProperties
    public String getMedicinalProperties() {
        return medicinalProperties;
    }

    public void setMedicinalProperties(String medicinalProperties) {
        this.medicinalProperties = medicinalProperties;
        Logger.log(Logger.LogLevel.INFO, getName() + " medicinal properties set to " + medicinalProperties + ".");
    }

    @Override
    public void grow() {
        super.grow();
        // Additional Tulsi-specific growth logic
        Logger.log(Logger.LogLevel.INFO, getName() + " is growing with medicinal properties: " + medicinalProperties + ".");
    }

    @Override
    public void dailyCheck() {
        super.dailyCheck();
        // Additional Tulsi-specific daily checks
        Logger.log(Logger.LogLevel.INFO, "Checking medicinal properties for " + getName() + ".");
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println(getName() + " requires specific care: Regular watering, adequate sunlight, trimming for medicinal growth, and pest management.");
    }
}
