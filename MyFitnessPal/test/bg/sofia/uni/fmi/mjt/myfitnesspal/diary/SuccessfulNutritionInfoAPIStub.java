package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;

import java.util.HashMap;
import java.util.Map;

public class SuccessfulNutritionInfoAPIStub implements NutritionInfoAPI {

    Map<String, NutritionInfo> infoForFood = new HashMap<>();
    static final NutritionInfo NI1 = new NutritionInfo(50, 40 ,10 );
    static final NutritionInfo NI2 = new NutritionInfo(10, 60 ,30 );
    static final String FOOD1 = "Pizza";
    static final String FOOD2 = "Spaghetti";

    public SuccessfulNutritionInfoAPIStub() {
        infoForFood.put(FOOD1, NI1);
        infoForFood.put(FOOD2, NI2);
    }
    @Override
    public NutritionInfo getNutritionInfo(String foodName) throws UnknownFoodException {
        return infoForFood.get(foodName);
    }
}
