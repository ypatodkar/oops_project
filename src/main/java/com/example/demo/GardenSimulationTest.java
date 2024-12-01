package com.example.demo;

public class GardenSimulationTest {
    public static void main(String[] args) {
        // Initialize the GardenSimulatorAPI
        GardenSimulatorAPI gardenAPI = new GardenSimulatorAPI();

        // Step 1: Initialize the garden from the config file
        String configFilePath = "./config.txt";
        gardenAPI.initializeGardenFromFile(configFilePath);

        // Step 2: Initialize controllers
        RainController rainController = new RainController(gardenAPI);
        SprinklerController sprinklerController = new SprinklerController(gardenAPI, 5); // Default water amount
        PesticideController pesticideController = new PesticideController(gardenAPI, 10); // Adjust as needed
        TemperatureController temperatureController = new TemperatureController(gardenAPI);

        // Step 3: Simulate a day
        System.out.println("Starting daily simulation...");
        rainController.simulateRandomRain(5, 15); // Simulate random rainfall
        temperatureController.simulateRandomTemperature(10, 40); // Simulate temperature change
        pesticideController.applyPesticideToPlant(gardenAPI.getPlantList().get(0)); // Apply pesticide to the first plant
        sprinklerController.autoWaterPlants(); // Auto water plants
        sprinklerController.waterAllPlants(); // Water all plants

        // Step 4: Check garden state
        System.out.println("Garden state:");
        System.out.println(gardenAPI.getState());

        // Step 5: Log the summary of plants
        System.out.println("Plant summary:");
        System.out.println(gardenAPI.getPlants());

        // Step 6: Log all events
        System.out.println("Event Log:");
        gardenAPI.getLog().forEach(System.out::println);

        System.out.println("Simulation complete!");
    }
}
