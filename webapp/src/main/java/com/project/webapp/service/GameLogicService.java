package com.project.webapp.service;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Logic for playing the game.
 * Created by LaMarinescu on 10/22/2016.
 */
public class GameLogicService {

    private static final Logger LOG = getLogger(GameLogicService.class);
    private final int[] pits = new int[14];
    private final String player1;
    private final String player2;

    public GameLogicService(final String player1, final String player2) {
        LOG.debug("Service created.");
        this.player1 = Objects.requireNonNull(player1);
        this.player2 = Objects.requireNonNull(player2);
        initializePits();
    }

    /**
     * Logic for updating the pits on current player turn.
     *
     * @param currentPlayer the current player
     * @param selectedId    the selected pit
     * @param selectedValue the value coresponding to selected pit
     * @return true, if the turn remain for the same player
     */
    public boolean turn(final String currentPlayer, final int selectedId, final int selectedValue) {

        int totalNumberOfStones = selectedId + selectedValue;
        LOG.debug("Stones: " + totalNumberOfStones);
        pits[selectedId] = 0;

        dumpPits();
        for (int i = selectedId + 1; i <= totalNumberOfStones; i++) {

            if (isPlayer1(currentPlayer)) {
                if (i % 14 != 0) {
                    pits[i % 14]++;
                } else {
                    totalNumberOfStones++;
                }
            } else {
                if (i % 14 != 7) {
                    pits[i % 14]++;
                } else {
                    totalNumberOfStones++;
                }
            }

        }
        dumpPits();
        LOG.debug("Stones after loop: " + totalNumberOfStones);
        if ((totalNumberOfStones % 14 == 7 && isPlayer1(currentPlayer)) ||
                (totalNumberOfStones % 14 == 0 && isPlayer2(currentPlayer))) {
            return true;
        }

        if (pits[totalNumberOfStones % 14] == 1 && totalNumberOfStones % 14 < 7
                && totalNumberOfStones > 0 && isPlayer1(currentPlayer)) {
            pits[7] = pits[7] + 1 + pits[14 - (totalNumberOfStones % 14)];
            pits[totalNumberOfStones % 14] = 0;
            pits[14 - (totalNumberOfStones % 14)] = 0;
        }

        if (pits[totalNumberOfStones % 14] == 1 && totalNumberOfStones % 14 > 7
                && totalNumberOfStones < 14 && isPlayer2(currentPlayer)) {
            pits[0] = pits[0] + 1 + pits[14 - (totalNumberOfStones % 14)];
            pits[totalNumberOfStones % 14] = 0;
            pits[14 - (totalNumberOfStones % 14)] = 0;
        }

        return false;
    }

    /**
     * Check if game is over.
     *
     * @return true, if game is over.
     */
    public boolean isOver() {
        LOG.debug("Sum Player1: " + Arrays.stream(pits).skip(1).limit(6).sum());
        LOG.debug("Sum Player2: " + Arrays.stream(pits).skip(8).limit(6).sum());
        return Arrays.stream(pits).skip(1).limit(6).sum() == 0 ||
                Arrays.stream(pits).skip(8).limit(6).sum() == 0;
    }

    /**
     * Retrieve winner.
     *
     * @return 1 - if player 1 won, 2 - if player 2 won, 0 - tie
     */
    public int retrieveWinner() {
        for (int i = 1; i <= 6; i++) {
            pits[7] += pits[i];
            pits[0] += pits[i + 7];
            pits[i] = 0;
            pits[i + 7] = 0;
        }

        if (pits[7] > pits[0]) {
            return 1;
        }

        if (pits[7] < pits[0]) {
            return 2;
        }

        return 0;
    }

    public int[] getPits() {
        return pits;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    /**
     * Initialize pits.
     */
    private void initializePits() {
        for (int i = 1; i <= 6; i++) {
            pits[i] = 6;
            pits[i + 7] = 6;
        }
        pits[7] = 0;
        pits[0] = 0;
    }

    private boolean isPlayer1(final String currentPlayer) {
        return player1.equals(currentPlayer);
    }

    /**
     * Check if current player is player 2.
     *
     * @param currentPlayer the current player
     * @return true, if current player is player 2
     */
    private boolean isPlayer2(final String currentPlayer) {
        return player2.equals(currentPlayer);
    }

    /**
     * Log pits.
     */
    private void dumpPits() {
        for (int i = 0; i < 14; i++) {
            LOG.debug(" " + pits[i] + " ");
        }
    }

}
