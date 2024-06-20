package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutritionInfoTest {

    @Test
    void testCalories() {
        NutritionInfo ni = new NutritionInfo(40, 30, 30);
        assertEquals(40 * 4 + 30 * 4 + 30 * 9, ni.calories(), "Nutrition info about calories is incorrect.");
    }

    @Test
    void testIfInfoIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-50, 40, 10), "Negative info.");
    }

    @Test
    void testIfInfoEquals100() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(100, 40, 10), "Sum is not equal to 100.");
    }
}