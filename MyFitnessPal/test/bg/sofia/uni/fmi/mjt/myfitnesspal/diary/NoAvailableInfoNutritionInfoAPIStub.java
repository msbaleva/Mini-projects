package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;

import java.util.HashMap;
import java.util.Map;

public class NoAvailableInfoNutritionInfoAPIStub implements NutritionInfoAPI {

    Map<String, NutritionInfo> infoForFood = new HashMap<>();


    @Override
    public NutritionInfo getNutritionInfo(String foodName) throws UnknownFoodException {
        throw new UnknownFoodException("Food " + foodName + " not found.");
    }
}
