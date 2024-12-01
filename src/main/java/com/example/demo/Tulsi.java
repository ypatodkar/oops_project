package com.example.demo;

import java.util.List;

public class Tulsi extends Plant {
    private boolean isMedicinal;
    private String medicinalUse;

    public Tulsi(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, boolean isMedicinal, String medicinalUse) {
        super(name, waterRequirement, pestVulnerabilities, tempLow, tempHigh);
        this.isMedicinal = isMedicinal;
        this.medicinalUse = medicinalUse;
    }

    public boolean isMedicinal() {
        return isMedicinal;
    }

    public String getMedicinalUse() {
        return medicinalUse;
    }

    @Override
    public void grow() {
        if (isAlive()) { // Use the getter method to check if the plant is alive
            System.out.println(getName() + " is growing! This medicinal plant needs special care.");
            // You can add specific growth behavior here
        } else {
            System.out.println(getName() + " is no longer alive and cannot grow.");
        }
    }

    @Override
    public void dailyCheck() {
        if (!isAlive()) {
            System.out.println(getName() + " is already dead and cannot be checked.");
            return;
        }

        if (getCurrentWaterLevel() < getWaterRequirement()) {
            System.out.println(getName() + " did not receive enough water and is now dead.");
            setAlive(false);
        } else {
            System.out.println(getName() + " is healthy and thriving.");
        }
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println("Tulsi needs daily watering and thrives in a warm environment.");
    }
}
