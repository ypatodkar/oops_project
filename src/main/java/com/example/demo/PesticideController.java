package com.example.demo;

import java.util.List;

public class PesticideController {
    private final GardenSimulatorAPI gardenAPI;
    private int pesticideEffectiveness; // Percentage of pests eliminated per application (1-100)

    // Constructor
    public PesticideController(GardenSimulatorAPI gardenAPI, int pesticideEffectiveness) {
        this.gardenAPI = gardenAPI;
        this.pesticideEffectiveness = Math.max(1, Math.min(pesticideEffectiveness, 100)); // Clamp to range 1-100
    }

    // Getter and Setter for effectiveness
    public int getPesticideEffectiveness() {
        return pesticideEffectiveness;
    }

    public void setPesticideEffectiveness(int pesticideEffectiveness) {
        this.pesticideEffectiveness = Math.max(1, Math.min(pesticideEffectiveness, 100));
    }

    /**
     * Apply pesticides to a specific plant to combat pests.
     *
     * @param plant The plant to treat with pesticides.
     */
    public void applyPesticideToPlant(Plant plant) {
        if (!plant.isAlive()) {
            System.out.println("Cannot apply pesticide. The plant " + plant.getName() + " is already dead.");
            Logger.log(Logger.LogLevel.INFO,"Pesticide application failed: " + plant.getName() + " is dead.");
            return;
        }

        System.out.println("Applying pesticide to " + plant.getName() + "...");
        Logger.log(Logger.LogLevel.INFO,"Pesticide applied to " + plant.getName());
        // Simulate pest removal - here it's assumed that all pests are removed for simplicity
        plant.setPestVulnerabilities(List.of());
        System.out.println("All pests removed from " + plant.getName() + ".");
        Logger.log(Logger.LogLevel.INFO, "Pests removed from " + plant.getName());
    }

    /**
     * Apply pesticides to all affected plants in the garden.
     */
    public void applyPesticideToAll() {
        System.out.println("Applying pesticides to all affected plants...");
        List<Plant> plants = gardenAPI.getPlantList();
        for (Plant plant : plants) {
            if (!plant.getPestVulnerabilities().isEmpty()) {
                applyPesticideToPlant(plant);
            }
        }
        System.out.println("Pesticide application complete.");
        Logger.log(Logger.LogLevel.INFO, "Pesticide applied to all affected plants.");
    }
}
