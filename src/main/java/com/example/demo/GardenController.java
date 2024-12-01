package com.example.demo;

import java.util.List;
import java.util.Map;

public class GardenController {
    private final GardenSimulatorAPI gardenAPI;

    // Constructor
    public GardenController() {
        this.gardenAPI = new GardenSimulatorAPI();
    }

    /**
     * Initialize the garden with a list of plants.
     *
     * @param initialPlants List of Plant objects to initialize the garden.
     */
    public void initializeGarden(List<Plant> initialPlants) {
        gardenAPI.initializeGarden(initialPlants);
        System.out.println("Garden initialized with " + initialPlants.size() + " plants.");
    }

    /**
     * Initialize the garden using a configuration file.
     *
     * @param filePath Path to the configuration file.
     */
    public void initializeGardenFromFile(String filePath) {
        gardenAPI.initializeGardenFromFile(filePath);
        System.out.println("Garden initialized from file: " + filePath);
    }

    /**
     * Add a plant to the garden.
     *
     * @param plant The plant to add.
     */
    public void addPlant(Plant plant) {
        gardenAPI.addPlant(plant);
        System.out.println("Plant added: " + plant.getName());
    }

    /**
     * Remove a plant from the garden by name.
     *
     * @param plantName Name of the plant to remove.
     */
    public void removePlant(String plantName) {
        gardenAPI.removePlant(plantName);
        System.out.println("Removed plant: " + plantName);
    }

    /**
     * Simulate rainfall.
     *
     * @param amount Amount of rainfall to simulate.
     */
    public void simulateRain(int amount) {
        if (amount < 0) {
            System.out.println("Error: Rainfall amount cannot be negative.");
            return;
        }
        gardenAPI.rain(amount);
        System.out.println("Rainfall of " + amount + " units simulated.");
    }

    /**
     * Simulate a temperature change.
     *
     * @param temperature Temperature to set.
     */
    public void simulateTemperature(int temperature) {
        gardenAPI.temperature(temperature);
        System.out.println("Temperature set to " + temperature + " degrees.");
    }

    /**
     * Simulate a pest attack.
     *
     * @param pest Name of the pest.
     */
    public void simulatePestAttack(String pest) {
        if (pest == null || pest.isEmpty()) {
            System.out.println("Error: Pest name cannot be empty.");
            return;
        }
        gardenAPI.pestAttack(pest);
        System.out.println("Pest attack simulated with: " + pest);
    }

    /**
     * Display the current state of the garden.
     */
    public void displayGardenState() {
        Map<String, Integer> state = gardenAPI.getState();
        System.out.println("Garden State:");
        System.out.println("Alive Plants: " + state.get("Alive Plants"));
        System.out.println("Dead Plants: " + state.get("Dead Plants"));
    }

    /**
     * Display the log of events.
     */
    public void displayLog() {
        List<String> log = gardenAPI.getLog();
        System.out.println("Event Log:");
        for (String entry : log) {
            System.out.println(entry);
        }
    }

    /**
     * Retrieve and display plant details.
     */
    public void displayPlantDetails() {
        Map<String, Object> plantDetails = gardenAPI.getPlants();
        System.out.println("Plant Details:");
        System.out.println("Names: " + plantDetails.get("Plants"));
        System.out.println("Water Requirements: " + plantDetails.get("Water Requirements"));
        System.out.println("Pest Vulnerabilities: " + plantDetails.get("Pest Vulnerabilities"));
    }
}
