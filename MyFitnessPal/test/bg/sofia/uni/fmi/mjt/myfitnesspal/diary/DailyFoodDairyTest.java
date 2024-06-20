package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyFoodDairyTest {

    static final NutritionInfoAPI NO_INFO_API_STUB = new NoAvailableInfoNutritionInfoAPIStub();
    static final NutritionInfoAPI SUCCESSFUL_INFO_API_STUB = new SuccessfulNutritionInfoAPIStub();
    static final String FOOD = "Pizza";
    static final String FOOD2 = "Spaghetti";
    static final FoodDiary NO_INFO_DIARY = new DailyFoodDiary(NO_INFO_API_STUB);
    static final FoodDiary SUCCESSFUL_INFO_DIARY = new DailyFoodDiary(SUCCESSFUL_INFO_API_STUB);
    static final double NEGATIVE_AMMOUNT = -2;
    static final double POSITIVE_AMMOUNT = 2;
    static final String EMPTY_STRING = "";
    static final String BLANK_STRING = "";
    static final String FOOD_WITH_NO_INFO = "Cauliflower";
    static final Meal MEAL = Meal.LUNCH;
    static final List<FoodEntry> FOOD_ENTRIES = new ArrayList<>();
    static final NutritionInfo NI1 = new NutritionInfo(50, 40 ,10 );
    static final NutritionInfo NI2 = new NutritionInfo(10, 60 ,30 );

    @BeforeAll
    static void setup() throws UnknownFoodException {
        SUCCESSFUL_INFO_DIARY.addFood(MEAL, FOOD, POSITIVE_AMMOUNT);
        SUCCESSFUL_INFO_DIARY.addFood(MEAL, FOOD2, POSITIVE_AMMOUNT);
        FOOD_ENTRIES.add(new FoodEntry(FOOD, POSITIVE_AMMOUNT, NI1));
        FOOD_ENTRIES.add(new FoodEntry(FOOD2, POSITIVE_AMMOUNT, NI2));

    }

    @Test
    void testIfAddedMealIsNull() {
        assertThrows(IllegalArgumentException.class, () -> SUCCESSFUL_INFO_DIARY.addFood(null, FOOD, POSITIVE_AMMOUNT));
    }

    @Test
    void testIfAddedFoodNameIsNullEmptyBlanc() {
        assertThrows(IllegalArgumentException.class, () -> SUCCESSFUL_INFO_DIARY.addFood(MEAL, null, POSITIVE_AMMOUNT));
        assertThrows(IllegalArgumentException.class, () -> SUCCESSFUL_INFO_DIARY.addFood(MEAL, EMPTY_STRING, POSITIVE_AMMOUNT));
        assertThrows(IllegalArgumentException.class, () -> SUCCESSFUL_INFO_DIARY.addFood(MEAL, BLANK_STRING, POSITIVE_AMMOUNT));
    }

    @Test
    void testIfAddedMealServingSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> SUCCESSFUL_INFO_DIARY.addFood(MEAL, FOOD, NEGATIVE_AMMOUNT));
    }

    @Test
    void testIfNoFoodInfo() {
        assertThrows(UnknownFoodException.class, () -> NO_INFO_DIARY.addFood(MEAL, FOOD_WITH_NO_INFO, POSITIVE_AMMOUNT));
    }


    @Test
    void testAddFood() throws UnknownFoodException {
        Collection<FoodEntry> actual = SUCCESSFUL_INFO_DIARY.getAllFoodEntries();
        Iterator<FoodEntry> iterExpected = FOOD_ENTRIES.iterator();
        Iterator<FoodEntry> iterActual = actual.iterator();
        while (iterActual.hasNext() && iterExpected.hasNext()) {
            assertEquals(iterExpected.next(), iterActual.next());
        }

        assertFalse(iterActual.hasNext() || iterExpected.hasNext());
    }

    @Test
    void testSortedByProteinContent() {
        Collection<FoodEntry> actual = SUCCESSFUL_INFO_DIARY.getAllFoodEntriesByProteinContent();
        FOOD_ENTRIES.sort(new FoodEntryProteinContentComparator());
        Iterator<FoodEntry> iterExpected = FOOD_ENTRIES.iterator();
        Iterator<FoodEntry> iterActual = actual.iterator();
        while (iterActual.hasNext() && iterExpected.hasNext()) {
            assertEquals(iterExpected.next(), iterActual.next());
        }

        assertFalse(iterActual.hasNext() || iterExpected.hasNext());
    }

    @Test
    void testDailyCaloriesIntake() {
         double expected = NI1.calories() * POSITIVE_AMMOUNT + NI2.calories() * POSITIVE_AMMOUNT;
         double actual = SUCCESSFUL_INFO_DIARY.getDailyCaloriesIntake();
         assertEquals(expected, actual, "Invalid daily calories intake.");
    }


}
