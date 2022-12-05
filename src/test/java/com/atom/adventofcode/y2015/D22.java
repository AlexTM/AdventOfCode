package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class D22 {

//    Magic Missile costs 53 mana. It instantly does 4 damage.
//    Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
//    Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
//    Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
//    Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.

    record Wizard(int hp, int mana){
        Wizard reduceMana(int mana) { return new Wizard(this.hp, this.mana-mana); }
        Wizard takeDamage(int hp) { return new Wizard(this.hp-hp, this.mana); }
    };
    record Boss(int hp, int damage){
        Boss takeDamage(int hp) { return new Boss(this.hp-hp, this.damage); }
    };
    record State(Wizard wizard, Boss boss, int wizardArmor){};
    record Spell(int manaCost, Effect effect){};

    interface Effect {
        State apply(State state, int age);
    }

    private static final Map<String, Spell> spells = Map.of(
            "Magic Missile", new Spell(53,(state, age) -> {
                if(age == 0) {
                    return new State(
                            state.wizard,
                            state.boss.takeDamage(4),
                            state.wizardArmor);
                }
                return state;
            }),
            "Drain", new Spell(73, (state, age) -> {
                if(age == 0) {
                    return new State(
                            state.wizard.takeDamage(-2),
                            state.boss.takeDamage(2),
                            state.wizardArmor);
                }
                return state;
            }),
            "Shield", new Spell(113, (state, age) -> {
                if(age < 6) {
                    return new State(
                            state.wizard,
                            state.boss,
                            state.wizardArmor+7);
                }
                return state;
            }),
            "Poison", new Spell(173, (state, age) -> {
                if(age < 6) {
                    return new State(
                            state.wizard,
                            state.boss.takeDamage(3),
                            state.wizardArmor);
                }
                return state;
            }),
            "Recharge", new Spell(229, (state, age) -> {
                if(age < 5) {
                    return new State(
                            state.wizard.reduceMana(-101),
                            state.boss,
                            state.wizardArmor);
                }
                return state;
            })
    );

    private int fightBoss(State state, Effect[] effectList, int depth, int manaSpent) {

//        System.out.println(state.boss);
        if(state.boss.hp <= 0) {
            // win
            return manaSpent;
        }

        int hp = state.wizard.hp - Math.max(1, state.boss.damage - state.wizardArmor);

        return fightWizard(
                new State(
                        state.wizard.takeDamage(hp),
                        state.boss, state.wizardArmor),
                effectList, depth+1, manaSpent);
    }

    // Could have one list of effects
    // position of the list will denote when the effect started
    // current length of the list will indicate the age of the effect
    private int fightWizard(State state, Effect[] effectList, int depth, int manaSpent) {

//        System.out.println(state.wizard);

        // check end conditions
        if(state.wizard.hp <= 0 || depth > effectList.length-1) {
            // lose
            return Integer.MAX_VALUE;
        }

        // Wizard cast spell
        int minMana = Integer.MAX_VALUE;

        for(Map.Entry<String, Spell> e : spells.entrySet()) {

            // cast spell
            Spell spell = e.getValue();
            if(state.wizard.mana < spell.manaCost) {
                continue;
            }
            int mana = manaSpent + spell.manaCost;
            Wizard newWizard = state.wizard.reduceMana(spell.manaCost);


            effectList[depth] = spell.effect;

            // Apply effects
            // A spell is cast every go, therefore list is the same length as depth
            State newState = new State(newWizard, state.boss, 0);
            for(int i=0; i<=depth; i++) {
                newState = effectList[i].apply(newState, depth - i);
            }

            minMana = Math.min(minMana,
                    fightBoss(newState, effectList, depth, mana));
        }

        return minMana;
    }

    // 173, 53

    @Test
    public void testFight() {
//        assertEquals(226, fightWizard(
//                new State(new Wizard(10, 250), new Boss(13, 8), 0),
//                new Effect[100],
//                0, 0
//        ));
        assertEquals(0, fightWizard(
                new State(new Wizard(50, 500), new Boss(55, 8), 0),
                new Effect[10],
                0, 0
        ));
    }


}
