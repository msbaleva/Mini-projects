package bg.sofia.uni.fmi.mjt.smartfridge.ingredient;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

public class DefaultIngredient <E extends Storable> implements Ingredient<E> {

    private E item;
    private int quantity;
    public DefaultIngredient(E item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public E item() {
        return item;
    }

    @Override
    public int quantity() {
        return quantity;
    }
}


//public record DefaultIngredient<E extends Storable>(E item, int quantity) implements Ingredient<E> {
//        }