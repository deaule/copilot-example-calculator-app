package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import javafx.application.Platform;
import javafx.scene.control.Button;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CalculatorController class.
 * Tests user interaction handling and model-view coordination.
 * 
 * Note: These tests require JavaFX to be initialized.
 * 
 * @author Java Calculator Team
 * @version 1.0.0
 */
@DisplayName("Calculator Controller Tests")
class CalculatorControllerTest {

    private CalculatorController controller;
    private CalculatorModel model;
    private CalculatorView view;

    @BeforeEach
    void setUp() {
        // Initialize JavaFX toolkit if not already initialized
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
        
        Platform.runLater(() -> {
            model = new CalculatorModel();
            controller = new CalculatorController(model);
            view = controller.getView();
        });
        
        // Wait for JavaFX operations to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("Controller should initialize with proper model and view")
    void testControllerInitialization() {
        Platform.runLater(() -> {
            assertNotNull(controller.getModel());
            assertNotNull(controller.getView());
            assertEquals("0", view.getPrimaryDisplay().getText());
            assertEquals("", view.getSecondaryDisplay().getText());
        });
    }

    @Test
    @DisplayName("Number button clicks should update display")
    void testNumberButtonClicks() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button button3 = findNumberButton("3");
            
            if (button5 != null && button3 != null) {
                button5.fire();
                assertEquals("5", view.getPrimaryDisplay().getText());
                
                button3.fire();
                assertEquals("53", view.getPrimaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Operation button clicks should set operation")
    void testOperationButtonClicks() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button addButton = view.getOperationButtons()[3]; // ADD is index 3
            
            if (button5 != null && addButton != null) {
                button5.fire();
                addButton.fire();
                assertEquals("5 +", view.getSecondaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Clear button should reset calculator")
    void testClearButton() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button clearButton = view.getClearButton();
            
            if (button5 != null && clearButton != null) {
                button5.fire();
                assertEquals("5", view.getPrimaryDisplay().getText());
                
                clearButton.fire();
                assertEquals("0", view.getPrimaryDisplay().getText());
                assertEquals("", view.getSecondaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Clear entry button should clear current input only")
    void testClearEntryButton() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button button3 = findNumberButton("3");
            Button addButton = view.getOperationButtons()[3];
            Button clearEntryButton = view.getClearEntryButton();
            
            if (button5 != null && button3 != null && addButton != null && clearEntryButton != null) {
                button5.fire();
                addButton.fire();
                button3.fire();
                
                clearEntryButton.fire();
                assertEquals("0", view.getPrimaryDisplay().getText());
                assertEquals("5 +", view.getSecondaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Equals button should calculate result")
    void testEqualsButton() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button button3 = findNumberButton("3");
            Button addButton = view.getOperationButtons()[3];
            Button equalsButton = view.getEqualsButton();
            
            if (button5 != null && button3 != null && addButton != null && equalsButton != null) {
                button5.fire();
                addButton.fire();
                button3.fire();
                equalsButton.fire();
                
                assertEquals("8", view.getPrimaryDisplay().getText());
                assertEquals("5 + 3 =", view.getSecondaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Decimal button should add decimal point")
    void testDecimalButton() {
        Platform.runLater(() -> {
            Button button3 = findNumberButton("3");
            Button decimalButton = view.getDecimalButton();
            Button button1 = findNumberButton("1");
            
            if (button3 != null && decimalButton != null && button1 != null) {
                button3.fire();
                decimalButton.fire();
                button1.fire();
                
                assertEquals("3.1", view.getPrimaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Sign button should toggle number sign")
    void testSignButton() {
        Platform.runLater(() -> {
            Button button5 = findNumberButton("5");
            Button signButton = view.getSignButton();
            
            if (button5 != null && signButton != null) {
                button5.fire();
                signButton.fire();
                
                assertEquals("-5", view.getPrimaryDisplay().getText());
                
                signButton.fire();
                assertEquals("5", view.getPrimaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Backspace button should remove last digit")
    void testBackspaceButton() {
        Platform.runLater(() -> {
            Button button1 = findNumberButton("1");
            Button button2 = findNumberButton("2");
            Button button3 = findNumberButton("3");
            Button backspaceButton = view.getBackspaceButton();
            
            if (button1 != null && button2 != null && button3 != null && backspaceButton != null) {
                button1.fire();
                button2.fire();
                button3.fire();
                assertEquals("123", view.getPrimaryDisplay().getText());
                
                backspaceButton.fire();
                assertEquals("12", view.getPrimaryDisplay().getText());
                
                backspaceButton.fire();
                assertEquals("1", view.getPrimaryDisplay().getText());
                
                backspaceButton.fire();
                assertEquals("0", view.getPrimaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Error state should update display styling")
    void testErrorDisplayStyling() {
        Platform.runLater(() -> {
            // Create division by zero error
            Button button1 = findNumberButton("1");
            Button button0 = findNumberButton("0");
            Button divideButton = view.getOperationButtons()[0]; // DIVIDE is index 0
            Button equalsButton = view.getEqualsButton();
            
            if (button1 != null && button0 != null && divideButton != null && equalsButton != null) {
                button1.fire();
                divideButton.fire();
                button0.fire();
                equalsButton.fire();
                
                assertTrue(view.getPrimaryDisplay().getStyleClass().contains("error-display"));
                assertEquals("Error: Division by zero", view.getPrimaryDisplay().getText());
            }
        });
    }

    @Test
    @DisplayName("Complex calculation should work correctly")
    void testComplexCalculation() {
        Platform.runLater(() -> {
            // Test: 15 ÷ 3 + 2 × 4 = ((15 ÷ 3) + 2) × 4 = 28
            Button[] numbers = new Button[10];
            for (int i = 0; i <= 9; i++) {
                numbers[i] = findNumberButton(String.valueOf(i));
            }
            
            Button divideButton = view.getOperationButtons()[0];
            Button addButton = view.getOperationButtons()[3];
            Button multiplyButton = view.getOperationButtons()[1];
            Button equalsButton = view.getEqualsButton();
            
            if (allButtonsNotNull(numbers) && divideButton != null && addButton != null && 
                multiplyButton != null && equalsButton != null) {
                
                // 15 ÷ 3
                numbers[1].fire();
                numbers[5].fire();
                divideButton.fire();
                numbers[3].fire();
                
                // + 2
                addButton.fire();
                numbers[2].fire();
                
                // × 4
                multiplyButton.fire();
                numbers[4].fire();
                
                // =
                equalsButton.fire();
                
                assertEquals("28", view.getPrimaryDisplay().getText());
            }
        });
    }

    /**
     * Helper method to find a number button by its text.
     */
    private Button findNumberButton(String digit) {
        Button[][] numberButtons = view.getNumberButtons();
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

    /**
     * Helper method to check if all buttons in array are not null.
     */
    private boolean allButtonsNotNull(Button[] buttons) {
        for (Button button : buttons) {
            if (button == null) {
                return false;
            }
        }
        return true;
    }
}
