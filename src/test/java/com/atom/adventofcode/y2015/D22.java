package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class D22 {

    class Agent{
        private int hp;

        public Agent(int hp) {
            this.hp = hp;
        }

    }

//    private Agent attack(Agent attacker, Agent defence) {
//        int hp = defence.hp - Math.max(1, attacker.damage - defence.armor);
//        return new Agent(hp, defence.damage, defence.armor);
//    }

    /*
    Magic Missile costs 53 mana. It instantly does 4 damage.
    Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
    Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
    Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
    Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
     */

    record Effect(int manaCost, int damage, int period, int delay, int heal, int armor, int manor) {}
    private static final Map<String, Effect> spells = Map.of(
            "Magic Missile", new Effect(53, 4, 0, 0, 0, 0, 0),
            "Drain", new Effect(73, 2, 0,0, 2, 0, 0),
            "Shield", new Effect(113, 0, 6, 0, 0, 7, 0),
            "Poison", new Effect(173, 3, 6, 0, 0, 0, 0),
            "Recharge", new Effect(229, 0, 5, 0, 0, 0, 101)
    );

    private void processEffect(Effect effect, Agent wizard, Agent boss) {
        boss.hp = boss.hp - effect.damage;
    }

/*    private boolean fight() {
        List<Effect> effectList = new ArrayList<>();
        while(true) {
            // wizard
            for(Map.Entry<String, Effect> e : spells.entrySet()) {
                effectList.add(e.getValue())
            }
        }
        return false;
    }*/

/*
    @Test
    public void testFight() {
        assertTrue(fight());
    }
*/

}
