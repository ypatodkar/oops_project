package com.example.demo;

import java.util.List;

public class GenericPlant extends Plant {
    public GenericPlant(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh) {
        super(name, waterRequirement, pestVulnerabilities, tempLow, tempHigh);
    }

    @Override
    public void grow() {
        System.out.println(getName() + " is growing in a generic way!");
    }

    @Override
    public void displaySpecialCareInstructions() {
        System.out.println(getName() + " requires generic care.");
    }

    @Override
    public void dailyCheck() {
        if (!isAlive()) {
            System.out.println(getName() + " is dead and cannot be checked.");
            return;
        }

        if (getCurrentWaterLevel() < getWaterRequirement()) {
            System.out.println(getName() + " did not receive enough water and died.");
            setAlive(false); // Mark the plant as dead
        } else {
            System.out.println(getName() + " is healthy and thriving.");
        }
    }
}
