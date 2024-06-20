package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    static final NutritionInfo NI = new NutritionInfo(10, 40, 50);
    static final String FOOD = "Pizza";
    @Test
    void testIfFoodIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(null, 10, NI), "Food is invalid/null.");
    }

    @Test
    void testIfServingSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(FOOD, -20, NI), "Negative serving size.");
    }

    @Test
    void testIfNutritionInfoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(FOOD, 10, null), "NutritionInfo is invalid/null.");
    }



}
