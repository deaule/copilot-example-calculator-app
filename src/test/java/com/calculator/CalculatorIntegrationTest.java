package com.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the Calculator application.
 * Tests the complete workflow from user input to displayed results.
 * 
 * @author Java Calculator Team
 * @version 1.0.0
 */
@DisplayName("Calculator Integration Tests")
class CalculatorIntegrationTest {

    private CalculatorModel model;

    @BeforeEach
    void setUp() {
        model = new CalculatorModel();
    }

    @Test
    @DisplayName("Complete calculation workflow should work correctly")
    void testCompleteCalculationWorkflow() {
        // Test: 15 + 25 * 2 = 80 (should be calculated as (15 + 25) * 2)

        // Input first number: 15
        model.inputDigit("1");
        model.inputDigit("5");
        assertEquals("15", model.getCurrentDisplay());

        // Set addition operation
        model.setOperation(CalculatorModel.Operation.ADD);
        assertEquals("15 +", model.getExpressionDisplay());

        // Input second number: 25
        model.inputDigit("2");
        model.inputDigit("5");
        assertEquals("25", model.getCurrentDisplay());

        // Chain with multiplication (should calculate 15 + 25 first)
        model.setOperation(CalculatorModel.Operation.MULTIPLY);
        assertEquals("40", model.getCurrentDisplay()); // 15 + 25 = 40
        assertEquals("40 ×", model.getExpressionDisplay());

        // Input third number: 2
        model.inputDigit("2");
        assertEquals("2", model.getCurrentDisplay());

        // Calculate final result
        model.calculate();
        assertEquals("80", model.getCurrentDisplay()); // 40 * 2 = 80
        assertEquals("40 × 2 =", model.getExpressionDisplay());
    }

    @Test
    @DisplayName("Error recovery workflow should work correctly")
    void testErrorRecoveryWorkflow() {
        // Create division by zero error
        model.inputDigit("1");
        model.inputDigit("0");
        model.setOperation(CalculatorModel.Operation.DIVIDE);
        model.inputDigit("0");
        model.calculate();

        assertTrue(model.hasError());
        assertEquals("Error: Division by zero", model.getCurrentDisplay());

        // Recovery: input new number should clear error
        model.inputDigit("5");
        assertFalse(model.hasError());
        assertEquals("5", model.getCurrentDisplay());
        assertEquals("", model.getExpressionDisplay());

        // Should be able to perform calculations normally after recovery
        model.setOperation(CalculatorModel.Operation.ADD);
        model.inputDigit("3");
        model.calculate();

        assertEquals("8", model.getCurrentDisplay());
        assertEquals("5 + 3 =", model.getExpressionDisplay());
    }

    @Test
    @DisplayName("Decimal number workflow should work correctly")
    void testDecimalWorkflow() {
        // Test: 3.14 + 2.86 = 6.00 (should display as 6)

        // Input 3.14
        model.inputDigit("3");
        model.inputDecimal();
        model.inputDigit("1");
        model.inputDigit("4");
        assertEquals("3.14", model.getCurrentDisplay());

        // Add operation
        model.setOperation(CalculatorModel.Operation.ADD);
        assertEquals("3.14 +", model.getExpressionDisplay());

        // Input 2.86
        model.inputDigit("2");
        model.inputDecimal();
        model.inputDigit("8");
        model.inputDigit("6");
        assertEquals("2.86", model.getCurrentDisplay());

        // Calculate
        model.calculate();
        assertEquals("6", model.getCurrentDisplay()); // Should strip trailing zeros
        assertEquals("3.14 + 2.86 =", model.getExpressionDisplay());
    }

    @Test
    @DisplayName("Negative number workflow should work correctly")
    void testNegativeNumberWorkflow() {
        // Test: -5 + 8 = 3

        // Input 5 and make it negative
        model.inputDigit("5");
        model.toggleSign();
        assertEquals("-5", model.getCurrentDisplay());

        // Add operation
        model.setOperation(CalculatorModel.Operation.ADD);
        assertEquals("-5 +", model.getExpressionDisplay());

        // Input 8
        model.inputDigit("8");
        assertEquals("8", model.getCurrentDisplay());

        // Calculate
        model.calculate();
        assertEquals("3", model.getCurrentDisplay());
        assertEquals("-5 + 8 =", model.getExpressionDisplay());
    }

    @Test
    @DisplayName("Clear operations workflow should work correctly")
    void testClearOperationsWorkflow() {
        // Set up some calculation state
        model.inputDigit("1");
        model.inputDigit("2");
        model.inputDigit("3");
        model.setOperation(CalculatorModel.Operation.ADD);
        model.inputDigit("4");
        model.inputDigit("5");
        model.inputDigit("6");

        assertEquals("456", model.getCurrentDisplay());
        assertEquals("123 +", model.getExpressionDisplay());

        // Test Clear Entry (CE) - should only clear current input
        model.clearEntry();
        assertEquals("0", model.getCurrentDisplay());
        assertEquals("123 +", model.getExpressionDisplay()); // Expression should remain

        // Enter new number
        model.inputDigit("7");
        model.inputDigit("8");
        model.inputDigit("9");
        assertEquals("789", model.getCurrentDisplay());

        // Test All Clear (AC) - should reset everything
        model.clear();
        assertEquals("0", model.getCurrentDisplay());
        assertEquals("", model.getExpressionDisplay());
        assertNull(model.getCurrentOperation());
    }

    @Test
    @DisplayName("Backspace workflow should work correctly")
    void testBackspaceWorkflow() {
        // Input a multi-digit number
        model.inputDigit("1");
        model.inputDigit("2");
        model.inputDigit("3");
        model.inputDigit("4");
        model.inputDigit("5");
        assertEquals("12345", model.getCurrentDisplay());

        // Test backspace operations
        model.backspace();
        assertEquals("1234", model.getCurrentDisplay());

        model.backspace();
        assertEquals("123", model.getCurrentDisplay());

        model.backspace();
        assertEquals("12", model.getCurrentDisplay());

        model.backspace();
        assertEquals("1", model.getCurrentDisplay());

        model.backspace();
        assertEquals("0", model.getCurrentDisplay());

        // Additional backspace should keep it at 0
        model.backspace();
        assertEquals("0", model.getCurrentDisplay());
    }

    @Test
    @DisplayName("Consecutive calculations workflow should work correctly")
    void testConsecutiveCalculations() {
        // First calculation: 5 + 3 = 8
        model.inputDigit("5");
        model.setOperation(CalculatorModel.Operation.ADD);
        model.inputDigit("3");
        model.calculate();
        assertEquals("8", model.getCurrentDisplay());

        // Second calculation using result: 8 * 2 = 16
        model.setOperation(CalculatorModel.Operation.MULTIPLY);
        model.inputDigit("2");
        model.calculate();
        assertEquals("16", model.getCurrentDisplay());

        // Third calculation using result: 16 / 4 = 4
        model.setOperation(CalculatorModel.Operation.DIVIDE);
        model.inputDigit("4");
        model.calculate();
        assertEquals("4", model.getCurrentDisplay());
    }

    @Test
    @DisplayName("Maximum input length should be enforced")
    void testMaximumInputLength() {
        // Try to input 20 digits (should be limited to 15)
        for (int i = 0; i < 20; i++) {
            model.inputDigit("1");
        }

        // Should only show 15 digits
        assertEquals("111111111111111", model.getCurrentDisplay());
        assertEquals(15, model.getCurrentDisplay().length());

        // Should still be able to perform operations
        model.setOperation(CalculatorModel.Operation.ADD);
        model.inputDigit("1");
        model.calculate();

        assertFalse(model.hasError());
        assertNotNull(model.getCurrentDisplay());
    }
}
