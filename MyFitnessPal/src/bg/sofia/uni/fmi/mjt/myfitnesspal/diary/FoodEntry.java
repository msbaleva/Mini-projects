package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;

import java.util.Objects;

public record FoodEntry(String food, double servingSize, NutritionInfo nutritionInfo) {

    public FoodEntry {
        if (food == null || food.isBlank()) {
            throw new IllegalArgumentException("Food cannot be null or blank");
        }

        if (servingSize < 0) { // bug: added this if
            throw new IllegalArgumentException("Invalid serving size");
        }

        if (nutritionInfo == null) {
            throw new IllegalArgumentException("Nutrition info cannot be null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodEntry foodEntry = (FoodEntry) o;
        return Double.compare(foodEntry.servingSize, servingSize) == 0 && Objects.equals(food, foodEntry.food) && Objects.equals(nutritionInfo, foodEntry.nutritionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, servingSize, nutritionInfo);
    }
}