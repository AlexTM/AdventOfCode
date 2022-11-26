package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D15 {

    record Ingredient(int capacity, int durability, int flavor, int texture, int calories){};

    // Frosting: capacity 4, durability -2, flavor 0, texture 0, calories 5
    //Candy: capacity 0, durability 5, flavor -1, texture 0, calories 8
    //Butterscotch: capacity -1, durability 0, flavor 5, texture 0, calories 6
    //Sugar: capacity 0, durability 0, flavor -2, texture 2, calories 1

    private static List<Ingredient> ingredientList = List.of(
            new Ingredient(4, -2, 0, 0, 5),
            new Ingredient(0, 5, -1, 0, 8),
            new Ingredient(-1, 0, 5, 0, 6),
            new Ingredient(0, 0, -2, 2, 1)
    );


    private int calculate(List<Ingredient> ingredients, List<Integer> values) {
        int []total = new int[]{0,0,0,0,0};
        for(int i=0; i<values.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            int v = values.get(i);

            total[0] += ingredient.capacity*v;
            total[1] += ingredient.durability*v;
            total[2] += ingredient.flavor*v;
            total[3] += ingredient.texture*v;
            total[4] += ingredient.calories*v;
        }
        return total[0]*total[1]*total[2]*total[3];
    }


    private int findBest(List<Ingredient> ingredients, List<Integer> values) {



        for(int i=0; i<100; i++) {
            findBest();
        }
    }

    @Test
    public void testCookie() {
        List<Ingredient> ingredients = List.of(
                new Ingredient(-1, -2, 6, 3, 8),
                new Ingredient(2, 3, -2, -1, 3));

        assertEquals(62842880, calculate(ingredients, List.of(44, 56)));
    }
}
