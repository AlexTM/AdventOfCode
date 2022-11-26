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

    private static final List<Ingredient> ingredientList = List.of(
            new Ingredient(4, -2, 0, 0, 5),
            new Ingredient(0, 5, -1, 0, 8),
            new Ingredient(-1, 0, 5, 0, 6),
            new Ingredient(0, 0, -2, 2, 1)
    );


    private int calculate(List<Ingredient> ingredients, int[] values, int calorieRequirement) {
        int []total = new int[]{0,0,0,0,0};
        for(int i=0; i<values.length; i++) {
            Ingredient ingredient = ingredients.get(i);
            int v = values[i];

            total[0] += ingredient.capacity*v;
            total[1] += ingredient.durability*v;
            total[2] += ingredient.flavor*v;
            total[3] += ingredient.texture*v;
            total[4] += ingredient.calories*v;
        }
        for(int i=0; i<5; i++)
            total[i] = Math.max(0, total[i]);

        // check if there is a calorie requirement
        if(calorieRequirement > 0 && total[4] != calorieRequirement)
            return Integer.MIN_VALUE;

        return total[0]*total[1]*total[2]*total[3];
    }


    private int findBest(
            List<Ingredient> ingredients, int[] values, int depth, int startPos, int currentValue, int calorieRequirement) {

        // Don't waste time when over 100
        if(currentValue >= 100)
            return Integer.MIN_VALUE;

        // All but last ingredients added, calculate the reminder for the last one
        if(depth == ingredients.size()-1) {
            values[depth] = 100 - currentValue;
            return calculate(ingredients, values, calorieRequirement);
        }

        int best = Integer.MIN_VALUE;
        for(int i=startPos; i<100; i++) {
            values[depth] = i;
            best = Math.max(best,
                    findBest(ingredients, values, depth+1, i+1, currentValue+i, calorieRequirement));
        }
        return best;
    }

    @Test
    public void testCookie() {
        List<Ingredient> ingredients = List.of(
                new Ingredient(-1, -2, 6, 3, 8),
                new Ingredient(2, 3, -2, -1, 3));

        assertEquals(62842880, calculate(ingredients, new int[]{44, 56}, -1));
        assertEquals(62842880, findBest(ingredients, new int[ingredients.size()], 0, 0, 0, -1));

        assertEquals(18965440, findBest(ingredientList, new int[ingredientList.size()], 0, 0, 0, -1));
        assertEquals(15862900, findBest(ingredientList, new int[ingredientList.size()], 0, 0, 0, 500));
    }
}
