package com.project.controller;

import com.project.factory.PlantFactory;
import com.project.factory.PlantType;
import com.project.modules.Plant;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class GardenSimulation extends Application {

    private final int GRID_SIZE = 8;
    private Button[][] gridButtons = new Button[GRID_SIZE][GRID_SIZE];
    private PlantType selectedPlantType = null;
    private Map<Button, Plant> plantMap = new HashMap<>();
    private Map<String, List<String>> pestVulnerabilities = new HashMap<>();
    private Map<String, Map<String, Integer>> insectDamageMap = new HashMap<>();
    private Map<Button, List<String>> activeInsectsMap = new HashMap<>();
    private TextArea logArea;
    private Label currentDayLabel;
    private Label currentDateLabel;
    private Label currentTimeLabel;
    private Label currentTempLabel;
    private int currentTemperature = 25; // Default temperature
    private static final int REAL_TO_GAME_TIME_MULTIPLIER = 24; // 1 real second = 24 game seconds


    //Day , time
    private int gameHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); // Fetch system hour (0-23)
    private int gameMinute = Calendar.getInstance().get(Calendar.MINUTE);    // Fetch system minute (0-59)
    private int gameSecond = Calendar.getInstance().get(Calendar.SECOND);    // Fetch system second (0-59)
    private int gameDay = 1;      // Current day in the game

    private boolean isIrrigationOn = false; // Tracks irrigation status
    private final int IRRIGATION_AMOUNT = 20; // Water level increase per irrigation
    private final int IRRIGATION_DURATION_MINUTES = 10; // Duration of irrigation in game minutes

    private final int RAINFALL_AMOUNT = 30; // Amount of water added during rainfall
    private final int RAINFALL_DURATION_MINUTES = 15; // Duration of rainfall in game minutes




    private Background gameBackground; // Background for the main layout


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();

            //Initializing which plants are attacked/ affected by which insects
        initializePestVulnerabilities();
        initializeInsectData();

        startGameTimeTimer();


        HBox topSection = initializeTopSection();
        VBox leftSidebar = initializeLeftSidebar();
        GridPane gardenGrid = initializeGardenGrid();

        VBox rightSection = new VBox();
        rightSection.setSpacing(10);
        rightSection.setPadding(new Insets(10));
        rightSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label logLabel = new Label("Event Log");
        logLabel.setFont(new Font("Arial", 16));
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefWidth(350);
        logArea.setPrefHeight(300);
        rightSection.getChildren().addAll(logLabel, logArea);

        mainLayout.setTop(topSection);
        mainLayout.setLeft(leftSidebar);
        mainLayout.setCenter(gardenGrid);
        mainLayout.setRight(rightSection);

        Scene scene = new Scene(mainLayout, 1200, 800);
        URL cssResource = getClass().getResource("/css/styles.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("CSS file not found. Please ensure that 'styles.css' is located in the '/css/' directory within 'resources'.");
        }

        primaryStage.setTitle("Garden Simulation System");
        primaryStage.setScene(scene);
        primaryStage.show();

        startWaterReductionTimer();
        startDayIncrementTimer();
        startGameClock(mainLayout); // Start the game clock
        startIrrigationTimer(); // Start the irrigation system
        startRainfallTimer(); //Start Rainfall Timer

    }

    private void initializePestVulnerabilities() {
        pestVulnerabilities.put(PlantType.ROSE.name(), Arrays.asList("Insect A", "Insect B"));
        pestVulnerabilities.put(PlantType.SUNFLOWER.name(), Arrays.asList("Insect A", "Insect C"));
        pestVulnerabilities.put(PlantType.ASHOKA.name(), Collections.singletonList("Insect D"));
        pestVulnerabilities.put(PlantType.MANGO.name(), Arrays.asList("Insect B", "Insect C"));
        pestVulnerabilities.put(PlantType.GANJA.name(), Collections.singletonList("Insect A"));
    }

    private void initializeInsectData() {
        insectDamageMap.put("Insect A", Map.of(PlantType.ROSE.name(), 10, PlantType.SUNFLOWER.name(), 15, PlantType.GANJA.name(), 5));
        insectDamageMap.put("Insect B", Map.of(PlantType.ROSE.name(), 8, PlantType.ASHOKA.name(), 12));
        insectDamageMap.put("Insect C", Map.of(PlantType.SUNFLOWER.name(), 10, PlantType.MANGO.name(), 7));
        insectDamageMap.put("Insect D", Map.of(PlantType.ASHOKA.name(), 20));
    }

    private void startRainfall() {
        logArea.appendText("[" + getGameTimeString() + "] Rainfall started.\n");

        // Increase water level for all plants
        for (Button gridButton : plantMap.keySet()) {
            Plant plant = plantMap.get(gridButton);
            if (plant != null) {
                plant.water(RAINFALL_AMOUNT);
                updateGridButtonText(gridButton, plant); // Update the UI
            }
        }

        // Stop rainfall after the duration
        Thread stopRainfallThread = new Thread(() -> {
            try {
                Thread.sleep(RAINFALL_DURATION_MINUTES * 1000L / REAL_TO_GAME_TIME_MULTIPLIER);
                Platform.runLater(() -> logArea.appendText("[" + getGameTimeString() + "] Rainfall stopped.\n"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stopRainfallThread.setDaemon(true);
        stopRainfallThread.start();
    }

    private void startRainfallTimer() {
        Thread rainfallThread = new Thread(() -> {
            Random random = new Random();

            while (true) {
                try {
                    // Wait for a random period between 1 to 3 real hours (equivalent to in-game hours)
                    int randomWaitTime = (1 + random.nextInt(3)) * 60 * 60 * 1000 / REAL_TO_GAME_TIME_MULTIPLIER;
                    Thread.sleep(randomWaitTime);

                    Platform.runLater(() -> {
                        // Trigger rainfall at the current game time
                        startRainfall();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        rainfallThread.setDaemon(true);
        rainfallThread.start();
    }



    private void startIrrigationTimer() {
        Thread irrigationThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 / REAL_TO_GAME_TIME_MULTIPLIER); // Sync with game time updates
                    Platform.runLater(() -> {
                        if ((gameHour == 6 || gameHour == 18) && gameMinute == 0 && !isIrrigationOn) {
                            startIrrigation();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        irrigationThread.setDaemon(true);
        irrigationThread.start();
    }


    private void startIrrigation() {
        isIrrigationOn = true;
        logArea.appendText("[" + getGameTimeString() + "] Automatic irrigation started.\n");

        // Increase water level for all plants
        for (Button gridButton : plantMap.keySet()) {
            Plant plant = plantMap.get(gridButton);
            if (plant != null) {
                plant.water(IRRIGATION_AMOUNT);
                updateGridButtonText(gridButton, plant); // Update UI
            }
        }

        // Schedule irrigation to stop after the duration
        Thread stopIrrigationThread = new Thread(() -> {
            try {
                Thread.sleep(IRRIGATION_DURATION_MINUTES * 1000L / REAL_TO_GAME_TIME_MULTIPLIER);
                Platform.runLater(this::stopIrrigation);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stopIrrigationThread.setDaemon(true);
        stopIrrigationThread.start();
    }

    private void stopIrrigation() {
        isIrrigationOn = false;
        irrigationStatusLabel.setText("Irrigation: OFF"); // Update UI status
        logArea.appendText("[" + getGameTimeString() + "] Automatic irrigation stopped.\n");
    }


    private String getGameTimeString() {
        String period = (gameHour < 12) ? "AM" : "PM";
        int displayHour = (gameHour % 12 == 0) ? 12 : gameHour % 12;

        return String.format("%02d:%02d:%02d %s", displayHour, gameMinute, gameSecond, period);
    }




    private void startGameClock(BorderPane mainLayout) {
        Thread gameClockThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // 1 real second
                    Platform.runLater(() -> {
                        // Increment game time
                        gameSecond += REAL_TO_GAME_TIME_MULTIPLIER;

                        if (gameSecond >= 60) {
                            gameSecond -= 60;
                            gameMinute++;
                        }

                        if (gameMinute >= 60) {
                            gameMinute -= 60;
                            gameHour++;
                        }

                        if (gameHour >= 24) {
                            gameHour -= 24;
                            gameDay++;
                            currentDayLabel.setText("Day: " + gameDay);
                        }

                        // Update time label
                        updateGameTimeLabel();

                        // Update greeting based on time
                        updateBackground(mainLayout);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameClockThread.setDaemon(true);
        gameClockThread.start();
    }


    private Label irrigationStatusLabel;

    private Label greetingLabel; // Declare at class level

    private HBox initializeTopSection() {
        HBox topSection = new HBox();
        topSection.setSpacing(20);
        topSection.setPadding(new Insets(10));
        topSection.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        currentDayLabel = new Label("Day: " + gameDay);

        currentTimeLabel = new Label(String.format(
                "Game Time: %02d:%02d:%02d %s",
                (gameHour % 12 == 0 ? 12 : gameHour % 12),
                gameMinute,
                gameSecond,
                gameHour < 12 ? "AM" : "PM"
        ));



        currentTempLabel = new Label("Current Temp: " + currentTemperature + "°C");
        irrigationStatusLabel = new Label("Irrigation: OFF");

        greetingLabel = new Label(); // Initialize the greeting label
        greetingLabel.setFont(new Font("Arial", 16)); // Style the greeting

        topSection.getChildren().addAll(currentDayLabel, currentTimeLabel, currentTempLabel, irrigationStatusLabel, greetingLabel);
        return topSection;
    }



    private VBox initializeLeftSidebar() {
        VBox leftSidebar = new VBox();
        leftSidebar.setSpacing(20);
        leftSidebar.setPadding(new Insets(20));

        VBox addPlantSection = createAddPlantSection();
        VBox addWaterSection = createAddWaterSection();
        VBox setTempSection = createSetTemperatureSection();
        VBox insectAttackSection = createInsectAttackSection();
        VBox applyPestControlSection = createApplyPestControlSection();

        leftSidebar.getChildren().addAll(addPlantSection, addWaterSection, setTempSection, insectAttackSection, applyPestControlSection);
        return leftSidebar;
    }

    private VBox createAddPlantSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        Label label = new Label("Add Plant");
        label.setFont(new Font("Arial", 16));

        // Initialize ToggleGroup
        ToggleGroup plantToggleGroup = new ToggleGroup();

        // Create ToggleButtons for each PlantType
        ToggleButton roseButton = new ToggleButton("🌹 Rose");
        roseButton.setToggleGroup(plantToggleGroup);
        roseButton.setUserData(PlantType.ROSE);
        roseButton.getStyleClass().add("plant-toggle-button"); // For CSS styling

        ToggleButton ashokaButton = new ToggleButton("🌲 Ashoka");
        ashokaButton.setToggleGroup(plantToggleGroup);
        ashokaButton.setUserData(PlantType.ASHOKA);
        ashokaButton.getStyleClass().add("plant-toggle-button");

        ToggleButton mangoButton = new ToggleButton("🥭 Mango");
        mangoButton.setToggleGroup(plantToggleGroup);
        mangoButton.setUserData(PlantType.MANGO);
        mangoButton.getStyleClass().add("plant-toggle-button");

        ToggleButton sunflowerButton = new ToggleButton("🌻 Sunflower");
        sunflowerButton.setToggleGroup(plantToggleGroup);
        sunflowerButton.setUserData(PlantType.SUNFLOWER);
        sunflowerButton.getStyleClass().add("plant-toggle-button");

        ToggleButton tulsiButton = new ToggleButton("🌿 Ganja");
        tulsiButton.setToggleGroup(plantToggleGroup);
        tulsiButton.setUserData(PlantType.GANJA);
        tulsiButton.getStyleClass().add("plant-toggle-button");

        // Listener to handle selection changes
        plantToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedPlantType = (PlantType) newToggle.getUserData();
                logArea.appendText(getCurrentTime() + ": Selected Plant Type: " + selectedPlantType.name() + "\n");
            } else {
                selectedPlantType = null;
                logArea.appendText(getCurrentTime() + ": No Plant Type Selected.\n");
            }
        });

        // Optional: Add a "Clear Selection" button
        Button clearSelectionButton = new Button("Clear Selection");
        clearSelectionButton.setOnAction(e -> plantToggleGroup.selectToggle(null));
        clearSelectionButton.setStyle("-fx-pref-width: 120px; -fx-pref-height: 40px;");

        section.getChildren().addAll(label, roseButton, ashokaButton, mangoButton, sunflowerButton, tulsiButton, clearSelectionButton);
        return section;
    }

    private VBox createAddWaterSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label label = new Label("Add Water");
        label.setFont(new Font("Arial", 16));

        Button addWaterButton = new Button("💧 Add Water");
        String existingStyle = addWaterButton.getStyle();
        addWaterButton.setStyle(existingStyle + " -fx-pref-width: 120px; -fx-pref-height: 50px;");


        addWaterButton.setOnAction(e -> showAddWaterPopup());

        section.getChildren().addAll(label, addWaterButton);
        return section;
    }

    private VBox createSetTemperatureSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label label = new Label("Set Temperature");
        label.setFont(new Font("Arial", 16));

        Button setTempButton = new Button("🌡️ Set Temp");
        setTempButton.setOnAction(e -> showSetTemperaturePopup());

        section.getChildren().addAll(label, setTempButton);
        return section;
    }

    private VBox createInsectAttackSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label label = new Label("Insect Attack");
        label.setFont(new Font("Arial", 16));

        Button insectAttackButton = new Button("🪲 Attack");
        insectAttackButton.setOnAction(e -> showInsectAttackPopup());

        section.getChildren().addAll(label, insectAttackButton);
        return section;
    }

    private VBox createApplyPestControlSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Label label = new Label("Apply Pest Control");
        label.setFont(new Font("Arial", 16));

        Button applyPestControlButton = new Button("🧴 Pest Control");
        applyPestControlButton.setOnAction(e -> showApplyPestControlPopup());

        section.getChildren().addAll(label, applyPestControlButton);
        return section;
    }

    private void showApplyPestControlPopup() {
        Stage popup = new Stage();
        popup.setTitle("Select Pest Control");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        CheckBox pestA = new CheckBox("🧴 Pest A");
        CheckBox pestB = new CheckBox("🧴 Pest B");
        CheckBox pestC = new CheckBox("🧴 Pest C");
        CheckBox pestD = new CheckBox("🧴 Pest D");

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(e -> {
            List<String> selectedPests = new ArrayList<>();
            if (pestA.isSelected()) selectedPests.add("Pest A");
            if (pestB.isSelected()) selectedPests.add("Pest B");
            if (pestC.isSelected()) selectedPests.add("Pest C");
            if (pestD.isSelected()) selectedPests.add("Pest D");

            if (!selectedPests.isEmpty()) {
                for (Button gridButton : activeInsectsMap.keySet()) {
                    List<String> activeInsects = activeInsectsMap.get(gridButton);
                    if (activeInsects != null && !activeInsects.isEmpty()) {
                        List<String> removedInsects = new ArrayList<>();
                        for (String insect : activeInsects) {
                            switch (insect) {
                                case "Insect A":
                                    if (selectedPests.contains("Pest A")) removedInsects.add(insect);
                                    break;
                                case "Insect B":
                                    if (selectedPests.contains("Pest B")) removedInsects.add(insect);
                                    break;
                                case "Insect C":
                                    if (selectedPests.contains("Pest C")) removedInsects.add(insect);
                                    break;
                                case "Insect D":
                                    if (selectedPests.contains("Pest D")) removedInsects.add(insect);
                                    break;
                            }
                        }
                        activeInsects.removeAll(removedInsects);
                        if (removedInsects.size() > 0) {
                            // Get the plant name
                            String plantName = plantMap.get(gridButton).getName();

                            // Log the pest control application
                            logArea.appendText(getCurrentTime() + ": Pest control applied for " + plantName + " at grid (" +
                                    ((int[]) gridButton.getUserData())[0] + ", " +
                                    ((int[]) gridButton.getUserData())[1] + "). Removed insects: " +
                                    String.join(", ", removedInsects) + "\n");

                            // Start health recovery for this plant
                            startHealthRecovery(gridButton);
                        }
                    }
                }
            }
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Pests to Apply:"), pestA, pestB, pestC, pestD, applyButton);

        Scene popupScene = new Scene(layout, 300, 250);
        popup.setScene(popupScene);
        popup.show();
    }

    private void startHealthRecovery(Button gridButton) {
        Plant plant = plantMap.get(gridButton);

        if (plant != null) {
            Thread healthRecoveryThread = new Thread(() -> {
                while (plant.getHealth() < 100) {
                    try {
                        Thread.sleep(15000); // 15 seconds interval
                        Platform.runLater(() -> {
                            plant.increaseHealth(5); // Increase health by 5%
                            updateGridButtonText(gridButton, plant);
                            logArea.appendText(getCurrentTime() + ": Health of " + plant.getName() +
                                    " at grid (" + ((int[]) gridButton.getUserData())[0] + ", " +
                                    ((int[]) gridButton.getUserData())[1] + ") increased by 5%. Current health: " + plant.getHealth() + "%\n");
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            healthRecoveryThread.setDaemon(true);
            healthRecoveryThread.start();
        }
    }

    private void showInsectAttackPopup() {
        Stage popup = new Stage();
        popup.setTitle("Select Insects");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        CheckBox insectA = new CheckBox("🪲 Insect A");
        CheckBox insectB = new CheckBox("🐜 Insect B");
        CheckBox insectC = new CheckBox("🦗 Insect C");
        CheckBox insectD = new CheckBox("🐞 Insect D");

        Button attackButton = new Button("Attack");
        attackButton.setOnAction(e -> {
            List<String> selectedInsects = new ArrayList<>();
            if (insectA.isSelected()) selectedInsects.add("Insect A");
            if (insectB.isSelected()) selectedInsects.add("Insect B");
            if (insectC.isSelected()) selectedInsects.add("Insect C");
            if (insectD.isSelected()) selectedInsects.add("Insect D");

            if (!selectedInsects.isEmpty()) {
                for (Button gridButton : plantMap.keySet()) {
                    Plant plant = plantMap.get(gridButton);
                    if (plant != null) {
                        for (String insect : selectedInsects) {
                            if (pestVulnerabilities.getOrDefault(plant.getPlantType().name(), new ArrayList<>()).contains(insect)) {
                                activeInsectsMap.computeIfAbsent(gridButton, k -> new ArrayList<>()).add(insect);
                                int damage = insectDamageMap.getOrDefault(insect, new HashMap<>()).getOrDefault(plant.getPlantType().name(), 0);
                                plant.decreaseHealth(damage);

                                // Retrieve row and column from UserData
                                int[] coordinates = (int[]) gridButton.getUserData();
                                int row = coordinates[0];
                                int col = coordinates[1];

                                updateGridButtonText(gridButton, plant);
                                logArea.appendText(getCurrentTime() + ": " + insect + " attacked " + plant.getName() +
                                        " at grid (" + row + ", " + col + "). Damage: " + damage + "\n");
                            }
                        }
                    }
                }
            }
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Insects to Attack:"), insectA, insectB, insectC, insectD, attackButton);

        Scene popupScene = new Scene(layout, 300, 250);
        popup.setScene(popupScene);
        popup.show();
    }

    private GridPane initializeGardenGrid() {
        GridPane gardenGrid = new GridPane();
        gardenGrid.setPadding(new Insets(70, 10, 10, 10)); // Top padding of 30px, right 10px, bottom 10px, left 10px
        gardenGrid.setHgap(5); // Spacing between columns
        gardenGrid.setVgap(5); // Spacing between rows

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button gridButton = new Button();
                gridButton.setPrefSize(120, 120);
                gridButton.setStyle("-fx-background-color: #8B4513; -fx-border-color: black; -fx-font-size: 35px;-fx-pref-width: 120px;-fx-pref-height: 70px;");
                gridButton.getStyleClass().add("grid-buttons");

                int row = i;
                int col = j;

                // Store row and column in button's UserData
                gridButton.setUserData(new int[]{row, col});

                gridButton.setOnMouseEntered(e -> {
                    if (gridButton.getText().isEmpty()) {
                        gridButton.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
//                        gridButton.setPrefSize(120, 120);

                    } else {
                        Plant plant = plantMap.get(gridButton);
                        if (plant != null) {
                            Tooltip tooltip = new Tooltip();

                            // Create a VBox to hold the health and water indicators
                            VBox tooltipContent = new VBox();
                            tooltipContent.setSpacing(5);

                            // Health Indicator
                            Label healthLabel = new Label("Health: " + plant.getHealth() + "%");
                            ProgressBar healthBar = new ProgressBar(plant.getHealth() / 100.0);
                            healthBar.setPrefWidth(150);
                            healthBar.getStyleClass().add("health-bar");

                            // Assign additional CSS classes based on health percentage
                            if (plant.getHealth() <= 30) {
                                healthBar.getStyleClass().add("low");
                            } else if (plant.getHealth() <= 70) {
                                healthBar.getStyleClass().add("medium");
                            }

                            // Water Indicator
                            Label waterLabel = new Label("Water: " + plant.getCurrentWaterLevel() + "%");
                            ProgressBar waterBar = new ProgressBar(plant.getCurrentWaterLevel() / 100.0);
                            waterBar.setPrefWidth(150);
                            waterBar.getStyleClass().add("water-bar");

                            // Assign additional CSS classes based on water percentage
                            if (plant.getCurrentWaterLevel() <= 30) {
                                waterBar.getStyleClass().add("low");
                            } else if (plant.getCurrentWaterLevel() <= 70) {
                                waterBar.getStyleClass().add("medium");
                            }

                            tooltipContent.getChildren().addAll(healthLabel, healthBar, waterLabel, waterBar);
                            tooltip.setGraphic(tooltipContent);

                            Tooltip.install(gridButton, tooltip);
                        }
                    }
                });

                gridButton.setOnMouseExited(e -> {
                    if (gridButton.getText().isEmpty()) {
                        gridButton.setStyle("-fx-background-color: #8B4513; -fx-border-color: black;-fx-pref-width: 120px;-fx-pref-height: 70px;");
//                        gridButton.setPrefSize(120, 120);
                    } else {
                        gridButton.setStyle("-fx-background-color: green; -fx-border-color: black;-fx-pref-width: 120px;-fx-pref-height: 70px;");
//                        gridButton.setPrefSize(120, 120);
                    }
                });

                gridButton.setOnAction(e -> {
                    if (selectedPlantType != null && gridButton.getText().isEmpty()) {
                        Plant plant = createPlant(selectedPlantType, "Plant " + (row * GRID_SIZE + col));
                        gridButton.setText(selectedPlantType.getEmoji());
                        gridButton.setStyle("-fx-background-color: green; -fx-border-color: black;-fx-pref-width: 120px;-fx-pref-height: 70px;");
                        plantMap.put(gridButton, plant);
                        logArea.appendText(getCurrentTime() + ": Planted " + plant.getName() + " at (" + row + ", " + col + ").\n");
                    } else if (!gridButton.getText().isEmpty()) {
                        logArea.appendText(getCurrentTime() + ": Grid (" + row + ", " + col + ") is already occupied.\n");
                    } else {
                        logArea.appendText(getCurrentTime() + ": No plant selected.\n");
                    }
                });

                gardenGrid.add(gridButton, j, i);
            }
        }
        return gardenGrid;
    }

    private void updateGridButtonText(Button gridButton, Plant plant) {
        if (plant != null) {
            StringBuilder buttonText = new StringBuilder();

            // Add plant emoji and its status
            buttonText.append(plant.getPlantType().getEmoji());

            // Add active insect emojis, if any
            List<String> activeInsects = activeInsectsMap.get(gridButton);
            if (activeInsects != null && !activeInsects.isEmpty()) {
                buttonText.append("\n");
                for (String insect : activeInsects) {
                    switch (insect) {
                        case "Insect A":
                            buttonText.append("🪲 ");
                            break;
                        case "Insect B":
                            buttonText.append("🐜 ");
                            break;
                        case "Insect C":
                            buttonText.append("🦗 ");
                            break;
                        case "Insect D":
                            buttonText.append("🐞 ");
                            break;
                    }
                }
            }



            gridButton.setText(buttonText.toString());

            gridButton.setStyle(" -fx-text-alignment: center; -fx-background-color: green; -fx-border-color: black; -fx-pref-width: 120px;-fx-pref-height: 70px;");
        }
    }

    private void showAddWaterPopup() {
        Stage popup = new Stage();
        popup.setTitle("Add Water");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        Spinner<Integer> waterSpinner = new Spinner<>(0, 100, 10);
        Button addWaterButton = new Button("Add Water");
        addWaterButton.setOnAction(e -> {
            int waterAmount = waterSpinner.getValue();
            for (Plant plant : plantMap.values()) {
                plant.water(waterAmount);
                logArea.appendText(getCurrentTime() + ": Added " + waterAmount + " units of water to " + plant.getName() + ".\n");
            }
            for (Button gridButton : plantMap.keySet()) {
                updateGridButtonText(gridButton, plantMap.get(gridButton));
            }
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Water Amount:"), waterSpinner, addWaterButton);

        Scene popupScene = new Scene(layout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    private void showSetTemperaturePopup() {
        Stage popup = new Stage();
        popup.setTitle("Set Temperature");

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        Spinner<Integer> tempSpinner = new Spinner<>(-50, 50, currentTemperature); // Spinner for temperature
        Button setTempButton = new Button("Set Temperature");
        setTempButton.setOnAction(e -> {
            currentTemperature = tempSpinner.getValue(); // Update temperature
            currentTempLabel.setText("Current Temp: " + currentTemperature + "°C"); // Update label
            logArea.appendText(getCurrentTime() + ": Temperature set to " + currentTemperature + "°C.\n");
            popup.close();
        });

        layout.getChildren().addAll(new Label("Select Temperature:"), tempSpinner, setTempButton);

        Scene popupScene = new Scene(layout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }


    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private Plant createPlant(PlantType type, String name) {
        List<String> vulnerabilities = pestVulnerabilities.getOrDefault(type.name(), new ArrayList<>());
        Object additionalParam = getAdditionalParamForPlantType(type);

        // Assign unique water requirements based on plant type
        int waterRequirement;
        switch (type) {
            case ROSE:
                waterRequirement = 3; // Example values
                break;
            case MANGO:
                waterRequirement = 7;
                break;
            case SUNFLOWER:
                waterRequirement = 5;
                break;
            case GANJA:
                waterRequirement = 4;
                break;
            case ASHOKA:
                waterRequirement = 6;
                break;
            default:
                waterRequirement = 5; // Default water requirement
        }

        // Create the plant
        // Create the plant with a meaningful name
        return PlantFactory.createPlant(
                type,
                type.name(), // Use the plant type's name as the plant's name, e.g., "Rose", "Mango"
                waterRequirement,
                vulnerabilities,
                15, // temperatureToleranceLow
                35, // temperatureToleranceHigh
                additionalParam
        );

    }


    private Object getAdditionalParamForPlantType(PlantType type) {
        switch (type) {
            case ROSE:
                return "Sweet Scent"; // Fragrance for Rose
            case ASHOKA:
                return "Strong Scent"; // Fragrance for Jasmine
            case MANGO:
                return 100; // fruitYield for Mango
            case GANJA:
                return "Anti-inflammatory"; // medicinalProperties for Tulsi
            case SUNFLOWER:
                return 50; // seedProduction for Sunflower
            default:
                return null; // No additional parameters required
        }
    }

    private void startDayIncrementTimer() {
        Thread dayIncrementThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3600000); // 3600 seconds = 1 hour of real time
                    Platform.runLater(() -> {
                        // Increment the day counter
                        int currentDay = Integer.parseInt(currentDayLabel.getText().replace("Day: ", ""));
                        currentDay++;
                        currentDayLabel.setText("Day: " + currentDay);

                        // Log the start of the new day
                        logArea.appendText(getCurrentTime() + ": A new day has started! Day " + currentDay + "\n");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        dayIncrementThread.setDaemon(true);
        dayIncrementThread.start();
    }

    private void startWaterReductionTimer() {
        Thread waterReductionThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); // 30 seconds interval
                    Platform.runLater(() -> {
                        Iterator<Map.Entry<Button, Plant>> iterator = plantMap.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Map.Entry<Button, Plant> entry = iterator.next();
                            Button gridButton = entry.getKey();
                            Plant plant = entry.getValue();

                            if (plant != null) {
                                // Calculate reduction based on plant's water requirement and temperature
                                int baseRate = plant.getWaterRequirement();
                                double temperatureMultiplier = 1 + (Math.abs(currentTemperature - 25) * 0.05); // Adjust rate based on temperature
                                int reduction = (int) Math.ceil(baseRate * temperatureMultiplier);

                                plant.decreaseWaterLevel(reduction);

                                // Retrieve row and column from UserData
                                int[] coordinates = (int[]) gridButton.getUserData();
                                int row = coordinates[0];
                                int col = coordinates[1];

                                if (plant.getCurrentWaterLevel() == 0) {
                                    plant.setHealth(0); // Set health to zero if water level is zero
                                }

                                if (plant.getHealth() == 0) {
                                    // Remove plant from the grid and backend
                                    gridButton.setText("");
                                    gridButton.setStyle("-fx-background-color: #8B4513; -fx-border-color: black;");
                                    iterator.remove(); // Remove the plant from the backend storage
                                    logArea.appendText(String.format("[%s] %s at grid (%d, %d) has died and been removed.\n",
                                            getCurrentTime(), plant.getName(), row, col));
                                } else {
                                    updateGridButtonText(gridButton, plant);
                                    logArea.appendText(String.format("[%s] Water level of %s at grid (%d, %d) reduced by %d units. Current water level: %d%%\n",
                                            getCurrentTime(), plant.getName(), row, col, reduction, plant.getCurrentWaterLevel()));
                                }
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waterReductionThread.setDaemon(true);
        waterReductionThread.start();
    }



    private void startGameTimeTimer() {
        Thread gameTimeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(150000); // 2.5 minutes = 1 game hour
                    Platform.runLater(() -> {
                        // Increment game time
                        gameHour = (gameHour + 1) % 24;
                        if (gameHour == 0) {
                            gameDay++; // Increment day when hour resets
                            currentDayLabel.setText("Day: " + gameDay);
                        }

                        // Update time display
                        updateGameTimeLabel();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameTimeThread.setDaemon(true);
        gameTimeThread.start();
    }

    private void updateGameTimeLabel() {
        String period = (gameHour < 12) ? "AM" : "PM";
        int displayHour = (gameHour % 12 == 0) ? 12 : gameHour % 12;

        currentTimeLabel.setText(String.format(
                "Game Time: %02d:%02d:%02d %s", displayHour, gameMinute, gameSecond, period
        ));
    }




    private void updateBackground(BorderPane mainLayout) {
        String greeting;

        if (gameHour >= 6 && gameHour < 12) {
            greeting = "☀️ Good Morning!";
        } else if (gameHour >= 12 && gameHour < 17) {
            greeting = "🌤️ Good Afternoon!";
        } else if (gameHour >= 17 && gameHour < 23) {
            greeting = "🌅 Good Evening!";
        } else {
            greeting = "🌙 Good Night!";
        }

        // Set a static background color
        mainLayout.setStyle("-fx-background-color: #45f5ee21;");

        // Update greeting
        greetingLabel.setText(greeting);
    }

}
