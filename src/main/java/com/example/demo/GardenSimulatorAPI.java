package com.example.demo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GardenSimulatorAPI {
    private List<Plant> plants;
    private Map<String, Integer> waterTracker;
    private List<String> log;
    private final String LOG_FILE = "log.txt"; // File to store logs

    public GardenSimulatorAPI() {
        this.plants = new ArrayList<>();
        this.waterTracker = new HashMap<>();
        this.log = new ArrayList<>();
    }

    // Initialize the garden with a list of plants
    public void initializeGarden(List<Plant> initialPlants) {
        plants.clear();
        plants.addAll(initialPlants);
        Logger.log(Logger.LogLevel.INFO, "Garden initialized with " + plants.size() + " plants.");
    }

    // Initialize the garden from a configuration file
    public void initializeGardenFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            plants.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                int waterRequirement = Integer.parseInt(parts[1].trim());
                List<String> pestVulnerabilities = Arrays.asList(parts[2].trim().split(";"));
                int tempLow = Integer.parseInt(parts[3].trim());
                int tempHigh = Integer.parseInt(parts[4].trim());

                // Dynamically create specific plant types
                switch (name.toLowerCase()) {
                    case "rose":
                        plants.add(new Rose(name,waterRequirement, pestVulnerabilities, tempLow, tempHigh));
                        break;
                    case "sunflower":
                        plants.add(new SunFlower(waterRequirement, pestVulnerabilities, tempLow, tempHigh, 100)); // Example height
                        break;
                    case "lily":
                        plants.add(new Lilly(name, waterRequirement, pestVulnerabilities, tempLow, tempHigh));
                        break;
                    default:
                        plants.add(new GenericPlant(name, waterRequirement, pestVulnerabilities, tempLow, tempHigh));
                        break;

                }
            }
            Logger.log(Logger.LogLevel.INFO, "Garden initialized from file: " + filePath);
        } catch (IOException | NumberFormatException e) {
            Logger.log(Logger.LogLevel.ERROR, "Error initializing garden from file: " + e.getMessage());
        }
    }


    // Get a summary of all plants in the garden
    public Map<String, Object> getPlants() {
        List<String> plantNames = new ArrayList<>();
        List<Integer> waterRequirements = new ArrayList<>();
        List<List<String>> pestVulnerabilities = new ArrayList<>();

        for (Plant plant : plants) {
            plantNames.add(plant.getName());
            waterRequirements.add(plant.getWaterRequirement());
            pestVulnerabilities.add(plant.getPestVulnerabilities());
        }

        Map<String, Object> plantSummary = new HashMap<>();
        plantSummary.put("Plants", plantNames);
        plantSummary.put("Water Requirements", waterRequirements);
        plantSummary.put("Pest Vulnerabilities", pestVulnerabilities);

        Logger.log(Logger.LogLevel.INFO,"Plant summary retrieved.");
        return plantSummary;
    }

    public List<Plant> getPlantList() {
        return plants; // Assuming `plants` is a List<Plant> in GardenSimulatorAPI
    }


    // Simulate rainfall
    public void rain(int amount) {
        if (amount < 0) {
            Logger.log(Logger.LogLevel.ERROR, "Invalid rainfall amount: " + amount);
            return;
        }
        for (Plant plant : plants) {
            plant.water(amount);
        }
        Logger.log(Logger.LogLevel.INFO,"Rainfall of " + amount + " units applied.");
    }

    // Simulate temperature changes
    public void temperature(int currentTemperature) {
        Logger.log(Logger.LogLevel.INFO,"Temperature set to " + currentTemperature + " degrees.");
        for (Plant plant : plants) {
            plant.adjustTemperature(currentTemperature);
        }
    }

    // Simulate pest attack
    public void pestAttack(String pest) {
        if (pest == null || pest.isEmpty()) {
            Logger.log(Logger.LogLevel.ERROR, "Invalid pest attack: Pest name is empty.");
            return;
        }
        for (Plant plant : plants) {
            plant.pestAttack(pest);
        }
        Logger.log(Logger.LogLevel.INFO,"Pest attack simulated with pest: " + pest);
    }

    // Get the current state of the garden
    public Map<String, Integer> getState() {
        int aliveCount = 0;
        int deadCount = 0;

        for (Plant plant : plants) {
            if (plant.isAlive()) {
                aliveCount++;
            } else {
                deadCount++;
            }
        }

        Logger.log(Logger.LogLevel.INFO,"Garden state retrieved: " + aliveCount + " alive, " + deadCount + " dead.");
        Map<String, Integer> state = new HashMap<>();
        state.put("Alive Plants", aliveCount);
        state.put("Dead Plants", deadCount);

        return state;
    }

    // Get the log of events
    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    // Add a new plant to the garden
    public void addPlant(Plant plant) {
        plants.add(plant);
        Logger.log(Logger.LogLevel.INFO,"Plant added: " + plant.getName());
    }

    // Remove a plant from the garden
    public void removePlant(String plantName) {
        boolean removed = plants.removeIf(plant -> plant.getName().equals(plantName));
        if (removed) {
            Logger.log(Logger.LogLevel.INFO,"Plant removed: " + plantName);
        } else {
            Logger.log(Logger.LogLevel.WARNING,"Plant not found: " + plantName);
        }
    }

    // Helper method to add a log entry with a timestamp
    private void addLog(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = "[" + timestamp + "] " + message;
        log.add(logEntry);
        writeLogToFile(logEntry);
    }

    // Helper method to write log entries to a file
    private void writeLogToFile(String logEntry) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
