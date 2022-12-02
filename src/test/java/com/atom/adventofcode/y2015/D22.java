/*
package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class D22 {


//    private Agent attack(Agent attacker, Agent defence) {
//        int hp = defence.hp - Math.max(1, attacker.damage - defence.armor);
//        return new Agent(hp, defence.damage, defence.armor);
//    }

    */
/*
    Magic Missile costs 53 mana. It instantly does 4 damage.
    Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
    Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
    Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
    Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
     *//*


    record Wizard(int hp, int mana){};
    record Boss(int hp, int damage){};
    record Effect(int manaCost, int damage, int period, int delay, int heal, int armor, int mana) {}

    private static final Map<String, Effect> spells = Map.of(
            "Magic Missile", new Effect(53, 4, 0, 0, 0, 0, 0),
            "Drain", new Effect(73, 2, 0,0, 2, 0, 0),
            "Shield", new Effect(113, 0, 6, 0, 0, 7, 0),
            "Poison", new Effect(173, 3, 6, 0, 0, 0, 0),
            "Recharge", new Effect(229, 0, 5, 0, 0, 0, 101)
    );

//    private static final Map<String, Function<State, State>> spells2 = Map.of(
//            "Magic Missile", (s) -> {}
//    );

//    private void processEffect(State state, Effect effect) {
//        state.boss.hp = state.boss.hp - effect.damage;
//    }

    class State {
        private List<Effect> effectList = new ArrayList<>();
        private Wizard wizard;
        private Boss boss;
//        public State
//        public State copy() {
//            return new State(new Wizard(wizard.hp, wizard.mana), new Boss(boss.hp, boss.damage));
//        }
    }

//    private List<Effect> deepCopy(List<Effect> effectList) {
//        List<Effect> copy = new ArrayList<>();
//        for(Effect e : effectList) {
//            copy.add(new Effect())
//        }
//        return copy;
//    }

    private void castSpell() {
        // wizard
        for(Map.Entry<String, Effect> e : spells.entrySet()) {

            // cast spell
            Effect effect = e.getValue();
            if(wizard.mana < effect.mana) {
                continue;
            }
            Wizard w = new Wizard(wizard.hp, wizard.mana - effect.mana);
            effectList.add(e.getValue());

            applyAllEffects(wizard, boss, effects)

            fight(w, b, effects, depth+1);

        }

    }

    // Could have one list of effects
    // position of the list will denote when the effect started
    // current length of the list will indicate the age of the effect
    private boolean fight(Wizard wizard, Boss boss, List<Effect> effectList, int depth) {
        if(depth % 2 == 0) {
            // Wizard turn
            castSpell();
        } else {
            // Boss turn
        }

        // check end conditions

        fight(wizard, boss, effectList, depth+1);

        return false;
    }


    @Test
    public void testFight() {
        State state = new State();
        state.wizard = new Wizard(50, 500);
        state.boss = new Boss(55, 8);
        assertTrue(fight(state));
    }


}
*/
