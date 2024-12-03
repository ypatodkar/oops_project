package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

public class GardenSimulationTest extends Application {

    private final int GRID_SIZE = 10;
    private Button[][] gridButtons = new Button[GRID_SIZE][GRID_SIZE];
    private String selectedPlant = null;
    private Map<String, String> plantEmojis = new HashMap<>();
    private TextArea logArea;
    private Label currentDayLabel;
    private Label currentDateLabel;
    private Label currentTimeLabel;
    private Label currentTempLabel;
    private int currentTemperature = 25; // Default temperature
    private Map<String, Plant> gridPlantMap = new HashMap<>(); // Map to track plants on the grid

    private GardenController gardenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<Plant> plants = new ArrayList<>();
        gardenController = new GardenController(plants);

        // Initialize plant emojis
        plantEmojis.put("Rose", "🌹");
        plantEmojis.put("Jasmine", "🌼");
        plantEmojis.put("Lily", "🌷");
        plantEmojis.put("Sunflower", "🌻");
        plantEmojis.put("Tulsi", "🌿");

        BorderPane mainLayout = new BorderPane();

        // Top Section
        HBox topSection = new HBox();
        topSection.setSpacing(20);
        topSection.setPadding(new Insets(10));
        topSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        currentDayLabel = new Label("Day: 1");
        currentDateLabel = new Label("Date: " + getCurrentDate());
        currentTimeLabel = new Label("Time: " + getCurrentTime());
        currentTempLabel = new Label("Current Temp: " + currentTemperature + "°C");

        topSection.getChildren().addAll(currentDayLabel, currentDateLabel, currentTimeLabel, currentTempLabel);

        // Update time regularly
        Thread timeUpdater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    currentTimeLabel.setText("Time: " + getCurrentTime());
                } catch (InterruptedException ignored) {
                }
            }
        });
        timeUpdater.setDaemon(true);
        timeUpdater.start();

        // Left Sidebar
        VBox leftSidebar = new VBox();
        leftSidebar.setSpacing(20);
        leftSidebar.setPadding(new Insets(20));

        // Add Plant Section
        VBox addPlantSection = new VBox();
        addPlantSection.setSpacing(10);
        addPlantSection.setPadding(new Insets(10));
        addPlantSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label addPlantLabel = new Label("Add Plant");
        addPlantLabel.setFont(new Font("Arial", 16));

        Button roseButton = new Button("🌹 Rose");
        roseButton.setOnAction(e -> selectedPlant = "Rose");

        Button jasmineButton = new Button("🌼 Jasmine");
        jasmineButton.setOnAction(e -> selectedPlant = "Jasmine");

        Button lilyButton = new Button("🌷 Lily");
        lilyButton.setOnAction(e -> selectedPlant = "Lily");

        Button sunflowerButton = new Button("🌻 Sunflower");
        sunflowerButton.setOnAction(e -> selectedPlant = "Sunflower");

        Button tulsiButton = new Button("🌿 Tulsi");
        tulsiButton.setOnAction(e -> selectedPlant = "Tulsi");

        addPlantSection.getChildren().addAll(addPlantLabel, roseButton, jasmineButton, lilyButton, sunflowerButton, tulsiButton);

        // Add Water Section
        VBox addWaterSection = new VBox();
        addWaterSection.setSpacing(10);
        addWaterSection.setPadding(new Insets(10));
        addWaterSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label addWaterLabel = new Label("Add Water");
        addWaterLabel.setFont(new Font("Arial", 16));

        Button addWaterButton = new Button("💧 Add Water");
        addWaterButton.setOnAction(e -> showAddWaterPopup());

        addWaterSection.getChildren().addAll(addWaterLabel, addWaterButton);

        // Set Temperature Section
        VBox setTempSection = new VBox();
        setTempSection.setSpacing(10);
        setTempSection.setPadding(new Insets(10));
        setTempSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label setTempLabel = new Label("Set Temperature");
        setTempLabel.setFont(new Font("Arial", 16));

        Button setTempButton = new Button("🌡️ Set Temp");
        setTempButton.setOnAction(e -> showSetTemperaturePopup());

        setTempSection.getChildren().addAll(setTempLabel, setTempButton);

        // Attack Section
        VBox attackSection = new VBox();
        attackSection.setSpacing(10);
        attackSection.setPadding(new Insets(10));
        attackSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label attackLabel = new Label("Attack");
        attackLabel.setFont(new Font("Arial", 16));

        Button attackButton = new Button("⚔️ Attack");
        attackButton.setOnAction(e -> showAttackPopup());

        attackSection.getChildren().addAll(attackLabel, attackButton);

        // Add all sections to sidebar
        leftSidebar.getChildren().addAll(addPlantSection, addWaterSection, setTempSection, attackSection);

        // Center Garden Grid
        GridPane gardenGrid = new GridPane();
        gardenGrid.setPadding(new Insets(10));
        gardenGrid.setHgap(5);
        gardenGrid.setVgap(5);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button gridButton = new Button();
                gridButton.setPrefSize(50, 50);
                gridButton.setStyle("-fx-background-color: #8B4513; -fx-border-color: black;");
                int row = i;
                int col = j;

                // Hover effects
                gridButton.setOnMouseEntered(e -> {
                    if (gridButton.getText().isEmpty()) {
                        gridButton.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
                    } else {
                        gridButton.setStyle("-fx-background-color: red; -fx-border-color: black;");
                    }
                });

                gridButton.setOnMouseExited(e -> {
                    if (gridButton.getText().isEmpty()) {
                        gridButton.setStyle("-fx-background-color: #8B4513; -fx-border-color: black;");
                    } else {
                        gridButton.setStyle("-fx-background-color: green; -fx-border-color: black;");
                    }
                });

                // Click event for planting
                gridButton.setOnAction(e -> {
                    if (selectedPlant != null && gridButton.getText().isEmpty()) {
                        // Create a new Plant object
                        Plant plant = new Plant(selectedPlant, currentTemperature, 10, List.of("Aphids", "Beetles"));
                        plant.setRow(row);
                        plant.setCol(col);

                        gardenController.getPlants().add(plant); // Add plant to the controller's list

                        // Update the grid button
                        gridButton.setText(plantEmojis.get(selectedPlant));
                        gridButton.setStyle("-fx-background-color: green; -fx-border-color: black;");
                        logArea.appendText("Placed " + selectedPlant + " at (" + row + ", " + col + ")\n");
                    } else if (!gridButton.getText().isEmpty()) {
                        logArea.appendText("Grid (" + row + ", " + col + ") is already occupied.\n");
                    } else {
                        logArea.appendText("No plant selected.\n");
                    }
                });

                gridButtons[i][j] = gridButton;
                gardenGrid.add(gridButton, j, i); // Add to gridpane with corrected order
            }
        }

        // Event Log Section
        VBox rightSection = new VBox();
        rightSection.setSpacing(10);
        rightSection.setPadding(new Insets(10));
        rightSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label logLabel = new Label("Event Log");
        logLabel.setFont(new Font("Arial", 16));
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefWidth(200);
        logArea.setPrefHeight(300);
        rightSection.getChildren().addAll(logLabel, logArea);

        // Set sections to the layout
        mainLayout.setTop(topSection);
        mainLayout.setLeft(leftSidebar);
        mainLayout.setCenter(gardenGrid);
        mainLayout.setRight(rightSection);

        // Set the scene and stage
        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setTitle("Garden Simulation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Show Add Water Popup
    private void showAddWaterPopup() {
        Stage popup = new Stage();
        popup.setTitle("Add Water");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        Spinner<Integer> waterSpinner = new Spinner<>(0, 100, 10);
        Button addWaterButton = new Button("Add Water");
        addWaterButton.setOnAction(e -> {
            logArea.appendText("Added " + waterSpinner.getValue() + " units of water.\n");
            popup.close();
        });


        layout.getChildren().addAll(new Label("Select Water Amount:"), waterSpinner, addWaterButton);

        Scene popupScene = new Scene(layout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    // Show Set Temperature Popup
    private void showSetTemperaturePopup() {
        Stage popup = new Stage();
        popup.setTitle("Set Temperature");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        Spinner<Integer> tempSpinner = new Spinner<>(-50, 50, currentTemperature);
        Button setTempButton = new Button("Set Temperature");
        setTempButton.setOnAction(e -> {
            currentTemperature = tempSpinner.getValue();
            currentTempLabel.setText("Current Temp: " + currentTemperature + "°C");
            logArea.appendText("Temperature set to " + currentTemperature + "°C.\n");
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Temperature:"), tempSpinner, setTempButton);

        Scene popupScene = new Scene(layout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    // Show Attack Popup
    private void showAttackPopup() {
        Stage popup = new Stage();
        popup.setTitle("Attack");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        CheckBox pestA = new CheckBox("Aphids");
        CheckBox pestB = new CheckBox("Caterpillars");
        CheckBox pestC = new CheckBox("Mites");
        CheckBox pestD = new CheckBox("Beetles");

        Button startAttackButton = new Button("Start Attack");
        startAttackButton.setOnAction(e -> {
            StringBuilder pestsSelected = new StringBuilder("Attacked pests: ");
            if (pestA.isSelected()) pestsSelected.append("Aphids ");
            if (pestB.isSelected()) pestsSelected.append("Caterpillars ");
            if (pestC.isSelected()) pestsSelected.append("Mites ");
            if (pestD.isSelected()) pestsSelected.append("Beetles ");
            logArea.appendText(pestsSelected.toString().trim() + ".\n");
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Pests to Attack:"), pestA, pestB, pestC, pestD, startAttackButton);

        Scene popupScene = new Scene(layout, 300, 250);
        popup.setScene(popupScene);
        popup.show();
    }

    // Utility method to get current date
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    // Utility method to get current time
    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}