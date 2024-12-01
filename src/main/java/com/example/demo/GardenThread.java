package com.example.demo;

public class GardenThread extends Thread {
    private final GardenSimulatorAPI gardenAPI;
    private final RainController rainController;
    private final TemperatureController temperatureController;
    private boolean running;

    // Constructor
    public GardenThread(GardenSimulatorAPI gardenAPI, RainController rainController, TemperatureController temperatureController) {
        this.gardenAPI = gardenAPI;
        this.rainController = rainController;
        this.temperatureController = temperatureController;
        this.running = true;
    }

    /**
     * Stop the thread gracefully.
     */
    public void stopThread() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Simulate a day's activities
                System.out.println("Day started in the garden.");

                // Random rain event
                rainController.simulateRandomRain(5, 15);

                // Random temperature event
                temperatureController.simulateRandomTemperature(50, 90);

                // Daily plant checks
                gardenAPI.getPlantList().forEach(Plant::dailyCheck);

                // Log garden state
                gardenAPI.getState();
                System.out.println("Garden state updated for the day.");

                // Pause for a day (e.g., 5 seconds in real-time for simulation purposes)
                Thread.sleep(5000); // Simulates one day

            } catch (InterruptedException e) {
                System.out.println("GardenThread interrupted: " + e.getMessage());
            }
        }
        System.out.println("Garden simulation stopped.");
    }
}
