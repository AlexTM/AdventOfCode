package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class D22 {

//    Magic Missile costs 53 manaUsed. It instantly does 4 damage.
//    Drain costs 73 manaUsed. It instantly does 2 damage and heals you for 2 hit points.
//    Shield costs 113 manaUsed. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
//    Poison costs 173 manaUsed. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
//    Recharge costs 229 manaUsed. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new manaUsed.

    record Wizard(int hp, int mana){
        Wizard reduceMana(int mana) { return new Wizard(this.hp, this.mana-mana); }
        Wizard takeDamage(int hp) { return new Wizard(this.hp-hp, this.mana); }
    };
    record Boss(int hp, int damage){
        Boss takeDamage(int hp) { return new Boss(this.hp-hp, this.damage); }
    };
    record Spell(int manaCost, Effect effect){};

    record State(Wizard wizard, Boss boss, int wizardArmor, int manaUsed, List<String> effectList){};
    static abstract class BuildState {
        static State copy(State s) { return new State(s.wizard,s.boss,s.wizardArmor,s.manaUsed, new ArrayList<>(s.effectList)); }
        static State copy(State s, Wizard w) { return new State(w,s.boss,s.wizardArmor,s.manaUsed, new ArrayList<>(s.effectList)); }
        static State copy(State s, Boss b) { return new State(s.wizard,b,s.wizardArmor,s.manaUsed, new ArrayList<>(s.effectList)); }
        static State copyArmor(State s, int a) { return new State(s.wizard,s.boss,a,s.manaUsed, new ArrayList<>(s.effectList)); }
        static State copyMana(State s, int a) { return new State(s.wizard,s.boss,s.wizardArmor,a, new ArrayList<>(s.effectList)); }
        static State copy(State s, Wizard w, Boss b) { return new State(w,b,s.wizardArmor,s.manaUsed, new ArrayList<>(s.effectList)); }
        static State copySpell(State s, String spell) {
            List<String> spells = new ArrayList<>(s.effectList);
            spells.add(spell);
            return new State(s.wizard,s.boss,s.wizardArmor,s.manaUsed,spells); }
    }

    interface Effect {
        State apply(State state, int age);
    }

    private String findEffect(Effect e) {
        for(Map.Entry<String, Spell> ee : spells.entrySet()) {
            if(e == ee.getValue().effect)
                return ee.getKey();
        }
        return null;
    }

    private void printState(Effect[] effects) {
        for(Effect e : effects) {
            if(e != null) {
                System.out.println(findEffect(e));
            }
        }
    }

    private static final Map<String, Spell> spells = Map.of(
            "Magic Missile", new Spell(53,(state, age) -> {
                if(age == 0) {
                    return BuildState.copy(state, state.boss.takeDamage(4));
                }
                return state;
            }),
            "Drain", new Spell(73, (state, age) -> {
                if(age == 0) {
                    return BuildState.copy(state, state.wizard.takeDamage(-2), state.boss.takeDamage(2));
                }
                return state;
            }),
            "Shield", new Spell(113, (state, age) -> {
                if(age < 6) {
                    return BuildState.copyArmor(state, state.wizardArmor+7);
                }
                return state;
            }),
            "Poison", new Spell(173, (state, age) -> {
                if(age < 6) {
                    return BuildState.copy(state, state.boss.takeDamage(3));
                }
                return state;
            }),
            "Recharge", new Spell(229, (state, age) -> {
                if(age < 5) {
                    return BuildState.copy(state, state.wizard.reduceMana(-101));
                }
                return state;
            })
    );


    // 173, 53

    private int fightWizard(State initialState) {

        int minManaUsed = Integer.MAX_VALUE;

        Queue<State> states = new LinkedList<>();
        states.add(initialState);
        int count = 0;

        while(!states.isEmpty() && count < 20) {
            final State state = states.poll();

            // Cast spell
            for(Map.Entry<String, Spell> entry : spells.entrySet()) {

                // Can wizard case spell
                if(state.wizard.mana < entry.getValue().manaCost)
                    continue;


                State newState = BuildState.copySpell(state, entry.getKey());
                newState = BuildState.copyMana(newState, entry.getValue().manaCost);

                // Apply spell effects
                for(int i=0; i<newState.effectList.size(); i++) {
                    String sn = newState.effectList.get(i);
                    Effect e = spells.get(sn).effect;
                    newState = e.apply(newState, count-i);
                }

                if(newState.boss.hp <= 0) {
                    // win
                    minManaUsed = Math.min(minManaUsed, newState.manaUsed);
                }

                // Boss attack
                int hp = newState.wizard.hp - Math.max(1, newState.boss.damage - newState.wizardArmor);
                newState = BuildState.copy(newState, newState.wizard.takeDamage(hp));

                // If still alive, this is a valid state to continue to search
                if(newState.wizard.hp > 0) {
                    states.add(newState);
                }
            }
            count++;
        }
        return minManaUsed;
    }

    @Test
    public void testFight() {

        int m = fightWizard(
                new State(
                        new Wizard(10, 250),
                        new Boss(13, 8),
                        0, 0, new ArrayList<>()));

        System.out.println("New min: "+m);
        System.out.println("----- Win -----");


        assertEquals(226, m);
//        assertEquals(0, fightWizard(
//                new State(new Wizard(50, 500), new Boss(55, 8), 0),
//                new Effect[10],
//                0, 0
//        ));
    }


}
