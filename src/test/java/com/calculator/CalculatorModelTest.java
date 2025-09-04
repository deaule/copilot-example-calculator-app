package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CalculatorModel class.
 * Tests all arithmetic operations, edge cases, and error handling.
 * 
 * @author Java Calculator Team
 * @version 1.0.0
 */
@DisplayName("Calculator Model Tests")
class CalculatorModelTest {

    private CalculatorModel calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalculatorModel();
    }

    @Test
    @DisplayName("Initial state should be zero")
    void testInitialState() {
        assertEquals("0", calculator.getCurrentDisplay());
        assertEquals("", calculator.getExpressionDisplay());
        assertFalse(calculator.hasError());
        assertEquals(BigDecimal.ZERO, calculator.getCurrentValue());
        assertNull(calculator.getCurrentOperation());
    }

    @Test
    @DisplayName("Clear should reset calculator to initial state")
    void testClear() {
        // Set up some state
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        
        // Clear and verify reset
        calculator.clear();
        
        assertEquals("0", calculator.getCurrentDisplay());
        assertEquals("", calculator.getExpressionDisplay());
        assertFalse(calculator.hasError());
        assertEquals(BigDecimal.ZERO, calculator.getCurrentValue());
        assertNull(calculator.getCurrentOperation());
    }

    @Test
    @DisplayName("Clear entry should only clear current input")
    void testClearEntry() {
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        
        calculator.clearEntry();
        
        assertEquals("0", calculator.getCurrentDisplay());
        assertEquals("5 +", calculator.getExpressionDisplay());
        assertEquals(CalculatorModel.Operation.ADD, calculator.getCurrentOperation());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"})
    @DisplayName("Should input single digits correctly")
    void testSingleDigitInput(String digit) {
        calculator.inputDigit(digit);
        assertEquals(digit.equals("0") ? "0" : digit, calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should input multiple digits correctly")
    void testMultipleDigitInput() {
        calculator.inputDigit("1");
        calculator.inputDigit("2");
        calculator.inputDigit("3");
        
        assertEquals("123", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should limit input to maximum display length")
    void testMaximumDisplayLength() {
        // Input 16 digits (over the 15 digit limit)
        for (int i = 0; i < 16; i++) {
            calculator.inputDigit("1");
        }
        
        // Should only show 15 digits
        assertEquals("111111111111111", calculator.getCurrentDisplay());
        assertEquals(15, calculator.getCurrentDisplay().length());
    }

    @Test
    @DisplayName("Should handle decimal point input")
    void testDecimalInput() {
        calculator.inputDigit("3");
        calculator.inputDecimal();
        calculator.inputDigit("1");
        calculator.inputDigit("4");
        
        assertEquals("3.14", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should ignore multiple decimal points")
    void testMultipleDecimalPoints() {
        calculator.inputDigit("3");
        calculator.inputDecimal();
        calculator.inputDigit("1");
        calculator.inputDecimal(); // Should be ignored
        calculator.inputDigit("4");
        
        assertEquals("3.14", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle decimal point as first input")
    void testDecimalAsFirstInput() {
        calculator.inputDecimal();
        calculator.inputDigit("5");
        
        assertEquals("0.5", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should toggle sign correctly")
    void testSignToggle() {
        calculator.inputDigit("5");
        calculator.toggleSign();
        
        assertEquals("-5", calculator.getCurrentDisplay());
        
        calculator.toggleSign();
        assertEquals("5", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should not toggle sign of zero")
    void testSignToggleZero() {
        calculator.toggleSign();
        assertEquals("0", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle backspace correctly")
    void testBackspace() {
        calculator.inputDigit("1");
        calculator.inputDigit("2");
        calculator.inputDigit("3");
        
        calculator.backspace();
        assertEquals("12", calculator.getCurrentDisplay());
        
        calculator.backspace();
        assertEquals("1", calculator.getCurrentDisplay());
        
        calculator.backspace();
        assertEquals("0", calculator.getCurrentDisplay());
        
        // Additional backspace should keep it at 0
        calculator.backspace();
        assertEquals("0", calculator.getCurrentDisplay());
    }

    @ParameterizedTest
    @CsvSource({
        "5, 3, ADD, 8",
        "10, 4, SUBTRACT, 6",
        "7, 3, MULTIPLY, 21",
        "15, 3, DIVIDE, 5"
    })
    @DisplayName("Should perform basic arithmetic operations correctly")
    void testBasicArithmetic(String first, String second, String operation, String expected) {
        // Input first number
        calculator.inputDigit(first);
        
        // Set operation
        CalculatorModel.Operation op = CalculatorModel.Operation.valueOf(operation);
        calculator.setOperation(op);
        
        // Input second number
        calculator.inputDigit(second);
        
        // Calculate
        calculator.calculate();
        
        assertEquals(expected, calculator.getCurrentDisplay());
        assertFalse(calculator.hasError());
    }

    @Test
    @DisplayName("Should handle addition correctly")
    void testAddition() {
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("8", calculator.getCurrentDisplay());
        assertEquals("5 + 3 =", calculator.getExpressionDisplay());
    }

    @Test
    @DisplayName("Should handle subtraction correctly")
    void testSubtraction() {
        calculator.inputDigit("1");
        calculator.inputDigit("0");
        calculator.setOperation(CalculatorModel.Operation.SUBTRACT);
        calculator.inputDigit("4");
        calculator.calculate();
        
        assertEquals("6", calculator.getCurrentDisplay());
        assertEquals("10 - 4 =", calculator.getExpressionDisplay());
    }

    @Test
    @DisplayName("Should handle multiplication correctly")
    void testMultiplication() {
        calculator.inputDigit("7");
        calculator.setOperation(CalculatorModel.Operation.MULTIPLY);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("21", calculator.getCurrentDisplay());
        assertEquals("7 × 3 =", calculator.getExpressionDisplay());
    }

    @Test
    @DisplayName("Should handle division correctly")
    void testDivision() {
        calculator.inputDigit("1");
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.DIVIDE);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("5", calculator.getCurrentDisplay());
        assertEquals("15 ÷ 3 =", calculator.getExpressionDisplay());
    }

    @Test
    @DisplayName("Should handle division by zero")
    void testDivisionByZero() {
        calculator.inputDigit("1");
        calculator.inputDigit("0");
        calculator.setOperation(CalculatorModel.Operation.DIVIDE);
        calculator.inputDigit("0");
        calculator.calculate();
        
        assertTrue(calculator.hasError());
        assertEquals("Error: Division by zero", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle decimal calculations")
    void testDecimalCalculations() {
        calculator.inputDigit("3");
        calculator.inputDecimal();
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("2");
        calculator.inputDecimal();
        calculator.inputDigit("5");
        calculator.calculate();
        
        assertEquals("6", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle negative number calculations")
    void testNegativeNumberCalculations() {
        calculator.inputDigit("5");
        calculator.toggleSign();
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("-2", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle chained operations")
    void testChainedOperations() {
        // 5 + 3 * 2 should be (5 + 3) * 2 = 16
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        calculator.setOperation(CalculatorModel.Operation.MULTIPLY);
        calculator.inputDigit("2");
        calculator.calculate();
        
        assertEquals("16", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle operations with large numbers")
    void testLargeNumbers() {
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.setOperation(CalculatorModel.Operation.MULTIPLY);
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.inputDigit("9");
        calculator.calculate();
        
        assertFalse(calculator.hasError());
        assertNotNull(calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should clear error state when new input is entered")
    void testErrorRecovery() {
        // Create error state
        calculator.inputDigit("1");
        calculator.setOperation(CalculatorModel.Operation.DIVIDE);
        calculator.inputDigit("0");
        calculator.calculate();
        
        assertTrue(calculator.hasError());
        
        // New digit input should clear error
        calculator.inputDigit("5");
        
        assertFalse(calculator.hasError());
        assertEquals("5", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle operation setting after calculation")
    void testOperationAfterCalculation() {
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("8", calculator.getCurrentDisplay());
        
        // Set new operation
        calculator.setOperation(CalculatorModel.Operation.MULTIPLY);
        assertEquals("8 ×", calculator.getExpressionDisplay());
    }

    @Test
    @DisplayName("Should handle repeated equals operations")
    void testRepeatedEquals() {
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("3");
        calculator.calculate();
        
        assertEquals("8", calculator.getCurrentDisplay());
        
        // Additional calculate calls should not change result
        calculator.calculate();
        assertEquals("8", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should handle zero operations correctly")
    void testZeroOperations() {
        calculator.inputDigit("0");
        calculator.setOperation(CalculatorModel.Operation.ADD);
        calculator.inputDigit("5");
        calculator.calculate();
        
        assertEquals("5", calculator.getCurrentDisplay());
        
        calculator.clear();
        calculator.inputDigit("5");
        calculator.setOperation(CalculatorModel.Operation.MULTIPLY);
        calculator.inputDigit("0");
        calculator.calculate();
        
        assertEquals("0", calculator.getCurrentDisplay());
    }

    @Test
    @DisplayName("Should format display numbers correctly")
    void testNumberFormatting() {
        calculator.inputDigit("1");
        calculator.inputDigit("0");
        calculator.inputDecimal();
        calculator.inputDigit("0");
        calculator.inputDigit("0");
        
        assertEquals("10.00", calculator.getCurrentDisplay());
        
        calculator.setOperation(CalculatorModel.Operation.DIVIDE);
        calculator.inputDigit("1");
        calculator.calculate();
        
        assertEquals("10", calculator.getCurrentDisplay()); // Should strip trailing zeros
    }
}
