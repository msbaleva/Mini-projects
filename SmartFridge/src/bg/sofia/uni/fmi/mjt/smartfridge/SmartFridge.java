package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.StorableComparator;

import java.util.*;

public class SmartFridge implements SmartFridgeAPI {

    private int totalCapacity;
    private int currentCapacity;
    private Map<String, Queue<Storable>> storage;
    public SmartFridge(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        this.storage = new HashMap<>();
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        if (item == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid item or quantity arguments.");
        }

        if (totalCapacity < quantity + currentCapacity) {
            throw new FridgeCapacityExceededException("Not even space in fridge.");
        }

        Queue<Storable> itemsToPut = storage.get(item.getName());
        if (itemsToPut == null) {
            itemsToPut= new PriorityQueue<>(new StorableComparator());
        }

        for (int i = 0; i < quantity; i++) {
            itemsToPut.add(item);
        }

        currentCapacity += quantity;

    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        if (isNotValid(itemName)) {;
            throw new IllegalArgumentException("Invalid item name.");
        }

        Queue<Storable> itemsToGet = storage.get(itemName);
        if (itemsToGet == null) {
            return new LinkedList<>();
        }

        currentCapacity -= itemsToGet.size();
        storage.remove(itemName);

        return new LinkedList<>(itemsToGet);
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        if (isNotValid(itemName) || quantity <= 0) {;
            throw new IllegalArgumentException("Invalid item name or quantity.");
        }

        Queue<Storable> itemsToGet = storage.get(itemName);
        if (itemsToGet == null || itemsToGet.size() < quantity) {
            throw new InsufficientQuantityException("Insufficient quantity.");
        }

        List<Storable> retrievedItems = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            retrievedItems.add(itemsToGet.poll());
        }

        if (itemsToGet.size() == 0) {
            storage.remove(itemName);
        }

        currentCapacity -= quantity;
        return retrievedItems;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (isNotValid(itemName)) {;
            throw new IllegalArgumentException("Invalid item name.");
        }

        Queue<Storable> items = storage.get(itemName);
        return  items == null ? 0 : items.size();
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Invalid recipe.");
        }

        Set<Ingredient<? extends Storable>> ingredients = recipe.getIngredients();
        List<Ingredient<? extends Storable>> missing = new LinkedList<>();
        for (Ingredient<? extends Storable> i : ingredients) {
            Queue<Storable> items = storage.get(i.item().getName());
            if (items == null) {
                missing.add(i);
                continue;
            }

            int availableQuantity = i.quantity();
            for (Storable s : items) {
                if (!s.isExpired()) {
                    break;
                }

                availableQuantity--;
            }

            if (i.quantity() > availableQuantity) {
                missing.add(new DefaultIngredient<>(i.item(), i.quantity() - availableQuantity));
            }
        }

        return missing.iterator();
    }

    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> expired = new LinkedList<>();
        for (String itemName : storage.keySet()) {
            Queue<Storable> items = storage.get(itemName);
            while (items.peek() != null && items.peek().isExpired()) {
                expired.add(items.poll());
                currentCapacity--;
            }
        }

        return expired;
    }

    private boolean isNotValid(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

}
