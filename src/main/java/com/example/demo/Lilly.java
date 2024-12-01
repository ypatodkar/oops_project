package com.example.demo;

import java.util.List;

public class Lilly extends Plant {
    public Lilly(String name, int waterRequirement, List<String> pestVulnerabilities, int tempLow, int tempHigh) {
        super(name, waterRequirement, pestVulnerabilities, tempLow, tempHigh);
    }

    @Override
    public void grow() {
        if (isAlive()) {
            System.out.println(getName() + " is growing and producing lovely flowers!");
            Logger.log(Logger.LogLevel.INFO, getName() + " is growing and producing lovely flowers.");
        } else {
            System.out.println(getName() + " is dead and cannot grow.");
            Logger.log(Logger.LogLevel.WARNING, getName() + " is dead and cannot grow.");
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
        System.out.println("Lilly requires frequent watering and prefers partial shade.");
    }
}
