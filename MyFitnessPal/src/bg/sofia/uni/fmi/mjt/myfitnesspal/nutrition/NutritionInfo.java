package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;

public record NutritionInfo(double carbohydrates, double fats, double proteins) {

    public static final double SUM_MACRO_NUTRIENTS = 100.0;
    public NutritionInfo {
        if (carbohydrates < 0 || fats < 0 || proteins < 0) {
            throw new IllegalArgumentException("Any nutrient in the nutrition info should be non-negative");
        }

        if (carbohydrates + fats + proteins != SUM_MACRO_NUTRIENTS) {
            throw new IllegalArgumentException("The sum of all nutrients should be 100");
        }
    }

    public double calories() {
        return proteins * MacroNutrient.PROTEIN.calories +
                fats * MacroNutrient.FAT.calories +
                carbohydrates * MacroNutrient.CARBOHYDRATE.calories;
    }


}
