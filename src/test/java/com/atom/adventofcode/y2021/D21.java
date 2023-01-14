package com.atom.adventofcode.y2021;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D21 {

    class Game {
        int[] playerScores = new int[]{0, 0};
        int[] playerPositions = new int[]{0, 0};

        int dieRoll = 0;
        int dieRolls = 0;

        public Game() {}

        public Game(Game game) {
            this.playerScores = Arrays.copyOf(game.playerScores, game.playerScores.length);
            this.playerPositions = Arrays.copyOf(game.playerPositions, game.playerPositions.length);
        }

        @Override
        public String toString() {
            return "Game{" +
                    "playerScores=" + Arrays.toString(playerScores) +
                    ", playerPositions=" + Arrays.toString(playerPositions) +
                    ", dieRoll=" + dieRoll +
                    '}';
        }
    }

    public int rollDie(Game game) {
        game.dieRolls++;
        game.dieRoll++;
        if(game.dieRoll == 101) {
            game.dieRoll = 1;
        }
        return game.dieRoll;
    }

    public void step(Game game, int player) {
        int spaces = rollDie(game)+rollDie(game)+rollDie(game);
        game.playerPositions[player] = (game.playerPositions[player] + spaces)%10;
        game.playerScores[player] += game.playerPositions[player] == 0 ? 10 : game.playerPositions[player];
    }

    public int playUntilWinner(Game game, Function<Integer, Boolean> winFunction) {
        int loser = -1;
        while(loser == -1) {
            for(int i=0; i<2; i++) {
                step(game, i);
                if(winFunction.apply(game.playerScores[i])) {
                    loser = i == 0 ? 1 : 0;
                    break;
                }
            }
        }
        return game.dieRolls * game.playerScores[loser];
    }

    @Test
    public void testPart1() {
        Game game = new Game();
        game.playerPositions = new int[]{4, 8};
        assertEquals(739785, playUntilWinner(game, i -> i >= 1000));

        // Player 1 starting position: 2
        // Player 2 starting position: 5
        game = new Game();
        game.playerPositions = new int[]{4, 10};
        assertEquals(855624, playUntilWinner(game, i -> i >= 1000));
    }

    // Not sure how to make this work.
    // Think ratios?

    public void step2(Game game, int player) {
        int spaces = rollDie(game)+rollDie(game)+rollDie(game);
        game.playerPositions[player] = (game.playerPositions[player] + spaces)%10;
        game.playerScores[player] += game.playerPositions[player] == 0 ? 10 : game.playerPositions[player];
    }

    class GameScores {
        long[] scores = new long[]{0,0};
        long played = 0;
    }

    public long universeWins(Game game) {
        GameScores gs = new GameScores();
        universeWinsRec(game, gs, 0, 3, 0);
        return Math.max(gs.scores[0], gs.scores[1]);
    }

    public void universeWinsRec(Game game, GameScores scores, int player, int rolls, int currentValue) {
        if(rolls == 0) {
            int newPos = (game.playerPositions[player] + currentValue)%10;
            int newScore = game.playerScores[player] + (newPos == 0 ? 10 : newPos);

            if(newScore >= 21) {
                scores.scores[player]++;
                scores.played++;
                if(scores.played%100000000 == 0){
                    System.out.println("Scores: "+ scores.scores[0]+", "+scores.scores[1]);
                    System.out.println("fraction: "+ (double)scores.scores[0]/(double)scores.scores[1]);
                }
            } else {
                Game newState = new Game(game);
                newState.playerPositions[player] = newPos;
                newState.playerScores[player] = newScore;
                universeWinsRec(newState, scores, player == 0 ? 1: 0, 3, 0);
            }
        } else {
            for(int spaces=1; spaces<4; spaces++) {
                universeWinsRec(game, scores, player, rolls - 1, currentValue + spaces);
            }
        }

    }


    // 444356092776315L
    // 2226830605
    @Test
    public void testPart2() {
        Game game = new Game();
        game.playerPositions = new int[]{4, 10};
        assertEquals(444356092776315L, universeWins(game));

//        universeWins(game);

        // Player 1 starting position: 2
        // Player 2 starting position: 5
//        game = new Game();
//        game.playerPositions = new int[]{2, 5};
//        assertEquals(576600, playUntilWinner(game));
    }

}
