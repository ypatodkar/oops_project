package com.project.factory;

import com.project.modules.*;
import com.project.logger.Logger;

import java.util.List;

/**
 * Factory class responsible for creating instances of Plant subclasses based on PlantType.
 */
public class PlantFactory {

    /**
     * Creates and returns an instance of a Plant subclass based on the provided PlantType.
     *
     * @param type                     The type of plant to create.
     * @param name                     The name of the plant.
     * @param waterRequirement         The water requirement of the plant.
     * @param pestVulnerabilities      A list of pests that can attack the plant.
     * @param temperatureToleranceLow  The lower bound of temperature tolerance.
     * @param temperatureToleranceHigh The upper bound of temperature tolerance.
     * @param additionalParams         Additional parameters required by specific PlantTypes.
     * @return An instance of a Plant subclass.
     * @throws IllegalArgumentException if the PlantType is unsupported or parameters are invalid.
     */
    public static Plant createPlant(
            PlantType type,
            String name,
            int waterRequirement,
            List<String> pestVulnerabilities,
            int temperatureToleranceLow,
            int temperatureToleranceHigh,
            Object... additionalParams // Varargs for additional parameters
    ) {
        switch (type) {
            case MANGO:
                if (additionalParams.length < 1) {
                    throw new IllegalArgumentException("Mango requires fruitYield parameter.");
                }
                int fruitYield;
                try {
                    fruitYield = (Integer) additionalParams[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameters for Mango. Expected integer for fruitYield.");
                }
                return new Mango(
                        name,
                        waterRequirement,
                        pestVulnerabilities,
                        temperatureToleranceLow,
                        temperatureToleranceHigh,
                        200,    // initialHeight in cm
                        10,     // growthRate in cm/day
                        fruitYield
                );

            case ROSE:
                if (additionalParams.length < 1) {
                    throw new IllegalArgumentException("Rose requires fragrance parameter.");
                }
                String fragrance;
                try {
                    fragrance = (String) additionalParams[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameters for Rose. Expected String for fragrance.");
                }
                return new Rose(
                        name,
                        waterRequirement,
                        pestVulnerabilities,
                        temperatureToleranceLow,
                        temperatureToleranceHigh,
                        20,     // initialDensity
                        30,     // trimmingFrequency in days
                        fragrance
                );

            case TULSI:
                if (additionalParams.length < 1) {
                    throw new IllegalArgumentException("Tulsi requires medicinalProperties parameter.");
                }
                String medicinalProperties;
                try {
                    medicinalProperties = (String) additionalParams[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameters for Tulsi. Expected String for medicinalProperties.");
                }
                return new Tulsi(
                        name,
                        waterRequirement,
                        pestVulnerabilities,
                        temperatureToleranceLow,
                        temperatureToleranceHigh,
                        10,                // initialDensity
                        20,                // trimmingFrequency in days
                        medicinalProperties
                );

            case ASHOKA:
                if (additionalParams.length < 1) {
                    throw new IllegalArgumentException("Ashoka requires floweringSeason parameter.");
                }
                String floweringSeason;
                try {
                    floweringSeason = (String) additionalParams[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameters for Ashoka. Expected String for floweringSeason.");
                }
                return new Ashoka(
                        name,
                        waterRequirement,
                        pestVulnerabilities,
                        temperatureToleranceLow,
                        temperatureToleranceHigh,
                        150,               // initialHeight in cm
                        8,                 // growthRate in cm/day
                        floweringSeason
                );

            case SUNFLOWER:
                if (additionalParams.length < 1) {
                    throw new IllegalArgumentException("Sunflower requires seedProduction parameter.");
                }
                int seedProduction;
                try {
                    seedProduction = (Integer) additionalParams[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameters for Sunflower. Expected integer for seedProduction.");
                }
                return new Sunflower(
                        name,
                        waterRequirement,
                        pestVulnerabilities,
                        temperatureToleranceLow,
                        temperatureToleranceHigh,
                        15,                // initialDensity
                        25,                // trimmingFrequency in days
                        seedProduction
                );
            default:
                Logger.log(Logger.LogLevel.ERROR, "Unsupported PlantType: " + type);
                throw new IllegalArgumentException("Unsupported PlantType: " + type);
        }
    }
}
