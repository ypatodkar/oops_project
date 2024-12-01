package com.example.demo;

import java.util.List;

public class SprinklerController {
    private final GardenSimulatorAPI gardenAPI;
    private int defaultWaterAmount; // Default amount of water for each watering event

    // Constructor
    public SprinklerController(GardenSimulatorAPI gardenAPI, int defaultWaterAmount) {
        this.gardenAPI = gardenAPI;
        this.defaultWaterAmount = defaultWaterAmount;
    }

    // Getter and Setter for defaultWaterAmount
    public int getDefaultWaterAmount() {
        return defaultWaterAmount;
    }

    public void setDefaultWaterAmount(int defaultWaterAmount) {
        this.defaultWaterAmount = defaultWaterAmount;
    }

    /**
     * Water a specific plant.
     *
     * @param plant The plant to water.
     * @param amount The amount of water to provide.
     */
    public void waterPlant(Plant plant, int amount) {
        if (!plant.isAlive()) {
            System.out.println("Cannot water " + plant.getName() + ". The plant is dead.");
            Logger.log(Logger.LogLevel.INFO,"Watering failed: " + plant.getName() + " is dead.");
            return;
        }

        plant.water(amount);
        System.out.println("Watered " + plant.getName() + " with " + amount + " units of water.");
        Logger.log(Logger.LogLevel.INFO, "Watered " + plant.getName() + " with " + amount + " units.");
    }

    /**
     * Water all plants in the garden with the default water amount.
     */
    public void waterAllPlants() {
        System.out.println("Watering all plants...");
        List<Plant> plants = gardenAPI.getPlantList();
        for (Plant plant : plants) {
            waterPlant(plant, defaultWaterAmount);
        }
        System.out.println("All plants have been watered.");
        Logger.log(Logger.LogLevel.INFO,"Watered all plants with " + defaultWaterAmount + " units each.");
    }

    /**
     * Check water levels and water plants if necessary.
     */
    public void autoWaterPlants() {
        System.out.println("Auto-watering plants based on water requirements...");
        List<Plant> plants = gardenAPI.getPlantList();
        for (Plant plant : plants) {
            if (plant.getCurrentWaterLevel() < plant.getWaterRequirement()) {
                int waterNeeded = plant.getWaterRequirement() - plant.getCurrentWaterLevel();
                waterPlant(plant, waterNeeded);
            }
        }
        System.out.println("Auto-watering completed.");
        Logger.log(Logger.LogLevel.INFO, "Auto-watering completed.");
    }
}
