package com.example.demo;

import java.util.List;

public class SunFlower extends Plant {
    private int height; // Unique property

    public SunFlower(int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh, int initialHeight) {
        super("Sunflower", waterRequirement, pestVulnerabilities, tempLow, tempHigh);
        this.height = initialHeight;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void grow() {
        if (isAlive()) { // Use the getter method instead of directly accessing the field
            height += 2; // Example of growth logic
            System.out.println(getName() + " is growing! Current height: " + height);
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
        System.out.println("Sunflowers need a lot of sunlight and space to grow tall.");
    }
}
