package com.calculator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View class for the calculator that creates and manages the user interface.
 * This class implements the visual components and layout of the calculator.
 * 
 * @author Java Calculator Team
 * @version 1.0.0
 */
public class CalculatorView extends VBox {
    
    private Label primaryDisplay;
    private Label secondaryDisplay;
    private GridPane buttonGrid;
    
    // Button references for styling and event handling
    private Button[][] numberButtons = new Button[4][3]; // 0-9 in grid layout
    private Button[] operationButtons = new Button[4]; // +, -, ×, ÷
    private Button equalsButton;
    private Button clearButton;
    private Button clearEntryButton;
    private Button backspaceButton;
    private Button decimalButton;
    private Button signButton;
    
    /**
     * Constructor creates and initializes the calculator view.
     */
    public CalculatorView() {
        initializeComponents();
        layoutComponents();
        styleComponents();
        setupKeyboardHandling();
    }
    
    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        // Create display labels
        primaryDisplay = new Label("0");
        secondaryDisplay = new Label("");
        
        // Create button grid
        buttonGrid = new GridPane();
        
        // Initialize buttons
        initializeButtons();
    }
    
    /**
     * Initializes all calculator buttons.
     */
    private void initializeButtons() {
        // Number buttons (0-9)
        for (int i = 0; i <= 9; i++) {
            Button button = new Button(String.valueOf(i));
            button.setUserData(i);
            if (i == 0) {
                numberButtons[3][1] = button; // 0 is in bottom row, middle
            } else {
                int row = 2 - (i - 1) / 3; // Calculate row (1-3 -> 2, 4-6 -> 1, 7-9 -> 0)
                int col = (i - 1) % 3;     // Calculate column
                numberButtons[row][col] = button;
            }
        }
        
        // Operation buttons
        operationButtons[0] = new Button("÷");
        operationButtons[1] = new Button("×");
        operationButtons[2] = new Button("-");
        operationButtons[3] = new Button("+");
        
        // Special function buttons
        clearButton = new Button("AC");
        clearEntryButton = new Button("CE");
        backspaceButton = new Button("←");
        decimalButton = new Button(".");
        signButton = new Button("±");
        equalsButton = new Button("=");
    }
    
    /**
     * Layouts all components using appropriate layout managers.
     */
    private void layoutComponents() {
        // Display area
        VBox displayArea = new VBox(5);
        displayArea.getChildren().addAll(secondaryDisplay, primaryDisplay);
        displayArea.setPadding(new Insets(10));
        
        // Button grid layout
        setupButtonGrid();
        
        // Main layout
        this.getChildren().addAll(displayArea, buttonGrid);
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        
        // Make button grid expand
        VBox.setVgrow(buttonGrid, Priority.ALWAYS);
    }
    
    /**
     * Sets up the button grid layout according to the specified design.
     */
    private void setupButtonGrid() {
        buttonGrid.setHgap(5);
        buttonGrid.setVgap(5);
        buttonGrid.setPadding(new Insets(10));
        
        // Row 0: [AC] [CE] [←] [÷]
        buttonGrid.add(clearButton, 0, 0);
        buttonGrid.add(clearEntryButton, 1, 0);
        buttonGrid.add(backspaceButton, 2, 0);
        buttonGrid.add(operationButtons[0], 3, 0);
        
        // Row 1: [7] [8] [9] [×]
        buttonGrid.add(numberButtons[0][0], 0, 1);
        buttonGrid.add(numberButtons[0][1], 1, 1);
        buttonGrid.add(numberButtons[0][2], 2, 1);
        buttonGrid.add(operationButtons[1], 3, 1);
        
        // Row 2: [4] [5] [6] [-]
        buttonGrid.add(numberButtons[1][0], 0, 2);
        buttonGrid.add(numberButtons[1][1], 1, 2);
        buttonGrid.add(numberButtons[1][2], 2, 2);
        buttonGrid.add(operationButtons[2], 3, 2);
        
        // Row 3: [1] [2] [3] [+]
        buttonGrid.add(numberButtons[2][0], 0, 3);
        buttonGrid.add(numberButtons[2][1], 1, 3);
        buttonGrid.add(numberButtons[2][2], 2, 3);
        buttonGrid.add(operationButtons[3], 3, 3);
        
        // Row 4: [±] [0] [.] [=]
        buttonGrid.add(signButton, 0, 4);
        buttonGrid.add(numberButtons[3][1], 1, 4);
        buttonGrid.add(decimalButton, 2, 4);
        buttonGrid.add(equalsButton, 3, 4);
        
        // Configure column constraints for equal width
        for (int i = 0; i < 4; i++) {
            buttonGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
            buttonGrid.getColumnConstraints().get(i).setPercentWidth(25);
        }
        
        // Configure row constraints for equal height
        for (int i = 0; i < 5; i++) {
            buttonGrid.getRowConstraints().add(new javafx.scene.layout.RowConstraints());
            buttonGrid.getRowConstraints().get(i).setPercentHeight(20);
        }
    }
    
    /**
     * Applies CSS styling and sets component properties.
     */
    private void styleComponents() {
        // Style display labels
        primaryDisplay.getStyleClass().add("primary-display");
        primaryDisplay.setAlignment(Pos.CENTER_RIGHT);
        primaryDisplay.setMaxWidth(Double.MAX_VALUE);
        
        secondaryDisplay.getStyleClass().add("secondary-display");
        secondaryDisplay.setAlignment(Pos.CENTER_RIGHT);
        secondaryDisplay.setMaxWidth(Double.MAX_VALUE);
        
        // Style buttons
        styleAllButtons();
        
        // Make this view focusable for keyboard input
        this.setFocusTraversable(true);
    }
    
    /**
     * Applies styling to all buttons.
     */
    private void styleAllButtons() {
        // Style number buttons
        for (int row = 0; row < numberButtons.length; row++) {
            for (int col = 0; col < numberButtons[row].length; col++) {
                if (numberButtons[row][col] != null) {
                    styleButton(numberButtons[row][col], "number-button");
                }
            }
        }
        
        // Style operation buttons
        for (Button button : operationButtons) {
            styleButton(button, "operation-button");
        }
        
        // Style special buttons
        styleButton(clearButton, "clear-button");
        styleButton(clearEntryButton, "clear-button");
        styleButton(backspaceButton, "clear-button");
        styleButton(decimalButton, "number-button");
        styleButton(signButton, "operation-button");
        styleButton(equalsButton, "equals-button");
    }
    
    /**
     * Applies styling to a single button.
     * 
     * @param button the button to style
     * @param styleClass the CSS style class to apply
     */
    private void styleButton(Button button, String styleClass) {
        button.getStyleClass().add(styleClass);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setMinSize(40, 40);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);
    }
    
    /**
     * Sets up keyboard event handling for the calculator.
     */
    private void setupKeyboardHandling() {
        this.setOnKeyPressed(this::handleKeyPressed);
    }
    
    /**
     * Handles keyboard input events.
     * 
     * @param event the key event
     */
    private void handleKeyPressed(KeyEvent event) {
        String keyText = event.getText();
        
        switch (event.getCode()) {
            case DIGIT0: case DIGIT1: case DIGIT2: case DIGIT3: case DIGIT4:
            case DIGIT5: case DIGIT6: case DIGIT7: case DIGIT8: case DIGIT9:
            case NUMPAD0: case NUMPAD1: case NUMPAD2: case NUMPAD3: case NUMPAD4:
            case NUMPAD5: case NUMPAD6: case NUMPAD7: case NUMPAD8: case NUMPAD9:
                // Handle number input
                if (keyText.matches("[0-9]")) {
                    Button numberButton = findNumberButton(keyText);
                    if (numberButton != null) {
                        numberButton.fire();
                    }
                }
                break;
                
            case PLUS: case ADD:
                operationButtons[3].fire(); // +
                break;
                
            case MINUS: case SUBTRACT:
                operationButtons[2].fire(); // -
                break;
                
            case MULTIPLY:
                operationButtons[1].fire(); // ×
                break;
                
            case DIVIDE: case SLASH:
                operationButtons[0].fire(); // ÷
                break;
                
            case PERIOD: case DECIMAL:
                decimalButton.fire();
                break;
                
            case ENTER: case EQUALS:
                equalsButton.fire();
                break;
                
            case ESCAPE:
                clearButton.fire(); // AC
                break;
                
            case DELETE:
                clearEntryButton.fire(); // CE
                break;
                
            case BACK_SPACE:
                backspaceButton.fire();
                break;
                
            default:
                // Handle character-based input for operations
                if (keyText.equals("=")) {
                    equalsButton.fire();
                } else if (keyText.equals(".")) {
                    decimalButton.fire();
                }
                break;
        }
        
        event.consume();
    }
    
    /**
     * Finds the number button corresponding to a digit string.
     * 
     * @param digit the digit as a string
     * @return the corresponding button, or null if not found
     */
    private Button findNumberButton(String digit) {
        for (int row = 0; row < numberButtons.length; row++) {
            for (int col = 0; col < numberButtons[row].length; col++) {
                Button button = numberButtons[row][col];
                if (button != null && button.getText().equals(digit)) {
                    return button;
                }
            }
        }
        return null;
    }
    
    // Getter methods for controller access
    
    public Label getPrimaryDisplay() { return primaryDisplay; }
    public Label getSecondaryDisplay() { return secondaryDisplay; }
    
    public Button[][] getNumberButtons() { return numberButtons; }
    public Button[] getOperationButtons() { return operationButtons; }
    public Button getEqualsButton() { return equalsButton; }
    public Button getClearButton() { return clearButton; }
    public Button getClearEntryButton() { return clearEntryButton; }
    public Button getBackspaceButton() { return backspaceButton; }
    public Button getDecimalButton() { return decimalButton; }
    public Button getSignButton() { return signButton; }
}
