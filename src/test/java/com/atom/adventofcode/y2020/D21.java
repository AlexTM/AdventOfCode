package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 21: Allergen Assessment ---
 * You reach the train's last stop and the closest you can get to your vacation island without getting wet. There
 * aren't even any boats here, but nothing can stop you now: you build a raft. You just need a few days' worth of
 * food for your journey.
 *
 * You don't speak the local language, so you can't read any ingredients lists. However, sometimes, allergens are
 * listed in a language you do understand. You should be able to use this information to determine which ingredient
 * contains which allergen and work out which foods are safe to take with you on your trip.
 *
 * You start by compiling a list of foods (your puzzle input), one food per line. Each line includes that food's
 * ingredients list followed by some or all of the allergens the food contains.
 *
 * Each allergen is found in exactly one ingredient. Each ingredient contains zero or one allergen. Allergens aren't
 * always marked; when they're listed (as in (contains nuts, shellfish) after an ingredients list), the ingredient
 * that contains each listed allergen will be somewhere in the corresponding ingredients list. However, even if an
 * allergen isn't listed, the ingredient that contains that allergen could still be present: maybe they forgot to
 * label it, or maybe it was labeled in a language you don't know.
 *
 * For example, consider the following list of foods:
 *
 * mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
 * trh fvjkl sbzzf mxmxvkd (contains dairy)
 * sqjhc fvjkl (contains soy)
 * sqjhc mxmxvkd sbzzf (contains fish)
 * The first food in the list has four ingredients (written in a language you don't understand): mxmxvkd, kfcds,
 * sqjhc, and nhms. While the food might contain other allergens, a few allergens the food definitely contains are
 * listed afterward: dairy and fish.
 *
 * The first step is to determine which ingredients can't possibly contain any of the allergens in any food in your
 * list. In the above example, none of the ingredients kfcds, nhms, sbzzf, or trh can contain an allergen. Counting
 * the number of times any of these ingredients appear in any ingredients list produces 5: they all appear once each
 * except sbzzf, which appears twice.
 *
 * Determine which ingredients cannot possibly contain any of the allergens in your list. How many times do any of
 * those ingredients appear?
 *
 * Your puzzle answer was 1958.
 *
 * --- Part Two ---
 * Now that you've isolated the inert ingredients, you should have enough information to figure out which ingredient
 * contains which allergen.
 *
 * In the above example:
 *
 * mxmxvkd contains dairy.
 * sqjhc contains fish.
 * fvjkl contains soy.
 * Arrange the ingredients alphabetically by their allergen and separate them by commas to produce your canonical
 * dangerous ingredient list. (There should not be any spaces in your canonical dangerous ingredient list.) In the
 * above example, this would be mxmxvkd,sqjhc,fvjkl.
 *
 * Time to stock your raft with supplies. What is your canonical dangerous ingredient list?
 *
 * Your puzzle answer was xxscc,mjmqst,gzxnc,vvqj,trnnvn,gbcjqbm,dllbjr,nckqzsg.
 */
public class D21 {

    record Item(List<String> items, List<String> allergens) {}

    public List<Item> loadMenu(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            String[] parts = line.split("\\(contains");
            String[] ing = parts[0].split(" ");
            String[] alg = parts[1].trim().substring(0, parts[1].length()-2).split(",");

            List<String> ingredientsList = Arrays.stream(ing).map(String::trim).collect(Collectors.toList());
            List<String> allergensList = Arrays.stream(alg).map(String::trim).filter(l -> !l.isBlank()).collect(Collectors.toList());
            return new Item(ingredientsList, allergensList);
        });
    }

    @Test
    public void testAllergens() {
        List<Item> items = loadMenu("src/test/resources/2020/D21_t.txt");
        Map<String, String> allergens = resolve(items);

        List<String> allItems = items.stream().flatMap(i -> i.items.stream()).collect(Collectors.toList());
        allItems.removeAll(allergens.values());
        assertEquals(5, allItems.size());


        items = loadMenu("src/test/resources/2020/D21.txt");
        allergens = resolve(items);

        allItems = items.stream().flatMap(i -> i.items.stream()).collect(Collectors.toList());
        allItems.removeAll(allergens.values());
        assertEquals(1958, allItems.size());
    }

    @Test
    public void testAllergens2() {
        List<Item> items = loadMenu("src/test/resources/2020/D21_t.txt");
        Map<String, String> allergens = resolve(items);
        List<String> allergenList = new ArrayList<>(allergens.keySet());
        allergenList.sort(Comparator.comparing(Function.identity()));
        List<String> res =
                allergenList.stream().map(allergens::get).collect(Collectors.toList());
        assertEquals("mxmxvkd,sqjhc,fvjkl", String.join(",", res));


        items = loadMenu("src/test/resources/2020/D21.txt");
        allergens = resolve(items);
        allergenList = new ArrayList<>(allergens.keySet());
        allergenList.sort(Comparator.comparing(Function.identity()));
        res =
                allergenList.stream().map(allergens::get).collect(Collectors.toList());
        assertEquals("xxscc,mjmqst,gzxnc,vvqj,trnnvn,gbcjqbm,dllbjr,nckqzsg", String.join(",", res));
    }

    public Map<String, String> resolve(List<Item> items) {
        Map<String, Set<String>> masterAllergenMap = new HashMap<>();
        for(Item i : items) {
            for(String all : i.allergens) {
                if(masterAllergenMap.containsKey(all)) {
                    masterAllergenMap.get(all).retainAll(new HashSet<>(i.items));
                } else {
                    masterAllergenMap.put(all, new HashSet<>(i.items));
                }
            }
        }

        Map<String, String> lookup = new HashMap<>();
        while(!masterAllergenMap.isEmpty()) {
            String next = null;
            for(Map.Entry<String, Set<String>> e : masterAllergenMap.entrySet()) {
                if(e.getValue().size() == 1) {
                    next = e.getKey();
                    break;
                }
            }
            if(next != null) {
                String ing = masterAllergenMap.get(next).iterator().next();
                lookup.put(next, ing);
                masterAllergenMap.remove(next);
                for(Set<String> s : masterAllergenMap.values()) {
                    s.remove(ing);
                }
            }
        }
        return lookup;
    }

}
