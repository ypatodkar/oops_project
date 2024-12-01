package com.example.demo;

import java.util.List;

public class Pest {
    private String name; // Name of the pest (e.g., Aphids, Mites)
    private String type; // Type of pest (e.g., Insect, Fungus)
    private List<String> affectedPlants; // List of plant names this pest can attack
    private int severityLevel; // Severity of the attack (1 to 10)
    private int lifespan; // Number of days the pest remains active

    // Constructor
    public Pest(String name, String type, List<String> affectedPlants, int severityLevel, int lifespan) {
        this.name = name;
        this.type = type;
        this.affectedPlants = affectedPlants;
        this.severityLevel = Math.max(1, Math.min(severityLevel, 10)); // Clamp severity between 1 and 10
        this.lifespan = lifespan; // Initialize lifespan
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAffectedPlants() {
        return affectedPlants;
    }

    public void setAffectedPlants(List<String> affectedPlants) {
        this.affectedPlants = affectedPlants;
    }

    public int getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(int severityLevel) {
        this.severityLevel = Math.max(1, Math.min(severityLevel, 10)); // Clamp severity
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    // Behavior Methods

    /**
     * Simulates a pest attack on a specific plant.
     *
     * @param plant The plant to attack.
     * @param gardenAPI Reference to the GardenSimulatorAPI for logging events.
     * @return True if the plant is affected, false otherwise.
     */
    public boolean attackPlant(Plant plant, GardenSimulatorAPI gardenAPI) {
        if (affectedPlants.contains(plant.getName())) {
            plant.pestAttack(this.name);
            String logEntry = "Pest " + name + " attacked " + plant.getName() + " with severity " + severityLevel + ".";
            Logger.log(Logger.LogLevel.INFO, logEntry);
            System.out.println(logEntry);
            return true;
        }
        String logEntry = "Pest " + name + " did not affect " + plant.getName() + ".";
        Logger.log(Logger.LogLevel.INFO, logEntry);
        System.out.println(logEntry);
        return false;
    }

    /**
     * Simulates a pest infestation across the entire garden.
     *
     * @param plants List of plants in the garden.
     * @param gardenAPI Reference to the GardenSimulatorAPI for logging events.
     */
    public void infestGarden(List<Plant> plants, GardenSimulatorAPI gardenAPI) {
        String logEntry = name + " is infesting the garden!";
        Logger.log(Logger.LogLevel.INFO, logEntry);

        System.out.println(logEntry);

        for (Plant plant : plants) {
            attackPlant(plant, gardenAPI);
        }
    }

    /**
     * Reduces the pest's lifespan by one day.
     *
     * @return True if the pest is still alive, false otherwise.
     */
    public boolean decrementLifespan() {
        if (lifespan > 0) {
            lifespan--;
            return lifespan > 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pest{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", affectedPlants=" + affectedPlants +
                ", severityLevel=" + severityLevel +
                ", lifespan=" + lifespan +
                '}';
    }
}
