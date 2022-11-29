package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D21 {

    record Item(String name, int cost, int damage, int armor){};

    List<Item> weapons = List.of(
            new Item("Dagger", 8, 4, 0),
            new Item("Shortsword", 10, 5, 0),
            new Item("Warhammer", 25, 6, 0),
            new Item("Longsword", 40, 7, 0),
            new Item("Greataxe", 74, 8, 0)
    );
    List<Item> armor = List.of(
            new Item("None", 0, 0, 0),
            new Item("Leather", 13, 0, 1),
            new Item("Chainmail", 31, 0, 2),
            new Item("Splintmail", 53, 0, 3),
            new Item("Bandedmail", 75, 0, 4),
            new Item("Platemail", 102, 0, 5)
    );
    List<Item> rings = List.of(
            new Item("None", 0, 0, 0),
            new Item("Damage +1", 25, 1, 0),
            new Item("Damage +2", 50, 2, 0),
            new Item("Damage +3", 100, 3, 0),
            new Item("Defense +1", 20, 0, 1),
            new Item("Defense +2", 40, 0, 2),
            new Item("Defense +3", 80, 0, 3)
    );

    record Agent(int hp, int damage, int armor){}

    private Agent attack(Agent attacker, Agent defence) {
        int hp = defence.hp - Math.max(1, attacker.damage - defence.armor);
        return new Agent(hp, defence.damage, defence.armor);
    }

    private boolean fight(Agent player, Agent boss) {
        while(true) {
            boss = attack(player, boss);
            if(boss.hp <= 0)
                return true;
            player = attack(boss, player);
            if(player.hp <= 0)
                return false;
        }
    }

    private int equip(List<List<Item>> items, int pos, List<Item> equipment, boolean forTheWin) {

        if(pos == items.size()) {
            return costAndFight(equipment, forTheWin);
        }

        int value = forTheWin ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for(Item item : items.get(pos)) {
            // recreating the list is ugly and expensive
            List<Item> newList = new ArrayList<>(equipment);
            // check if equipment already contains item
            if(equipment.contains(item))
                continue;
            newList.add(item);
            if(forTheWin)
                value = Math.min(value, equip(items, pos+1, newList, true));
            else
                value = Math.max(value, equip(items, pos+1, newList, false));
        }
        return value;
    }

    private int costAndFight(List<Item> itemList, boolean forTheWin) {
        // 3 streams!
        int damage = itemList.stream().mapToInt(i -> i.damage).sum();
        int armor = itemList.stream().mapToInt(i -> i.armor).sum();
        int cost = itemList.stream().mapToInt(i -> i.cost).sum();

        if(fight(new Agent(100, damage, armor), boss)) {
            return forTheWin ? cost : Integer.MIN_VALUE;
        }
        return forTheWin ? Integer.MAX_VALUE : cost;
    }

    private static final Agent boss = new Agent(109, 8, 2);

    @Test
    public void testEquip() {
        assertEquals(111, equip(
                List.of(weapons, armor, rings, rings), 0, new ArrayList<>(), true));
        assertEquals(188, equip(
                List.of(weapons, armor, rings, rings), 0, new ArrayList<>(), false));
    }
}
