package com.project.webapp.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link GameLogicService}
 * Created by LaMarinescu on 10/25/2016.
 */
public class GameLogicServiceTest {

    private static final String PLAYER1_NAME = "john";
    private static final String PLAYER2_NAME = "doe";

    @Test
    public void isStillSamePlayersTurn() {

        //Given
        final GameLogicService gameLogicService = new GameLogicService("john", "doe");

        //When
        final boolean checkTurn = gameLogicService.turn("john", 1, 6);

        //Then
        Assert.assertTrue(checkTurn);

    }

    @Test
    public void isOpponentPlayersTurn() {

        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        //When
        final boolean checkTurn = gameLogicService.turn(PLAYER1_NAME, 2, 6);

        //Then
        Assert.assertFalse(checkTurn);

    }

    @Test
    public void gameIsOver() {
        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        //When
        for (int i = 1; i <= 6; i++) {
            gameLogicService.getPits()[i] = 0;
        }

        final boolean isOver = gameLogicService.isOver();

        //Then
        Assert.assertTrue(isOver);
    }

    @Test
    public void gameIsNotOver() {
        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        final boolean isOver = gameLogicService.isOver();

        //Then
        Assert.assertFalse(isOver);
    }

    @Test
    public void IsATie() {
        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        //When
        final int winner = gameLogicService.retrieveWinner();

        //Then
        Assert.assertEquals(0, winner);

    }

    @Test
    public void player1Won() {
        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        gameLogicService.getPits()[13] = 5;

        //When
        final int winner = gameLogicService.retrieveWinner();

        //Then
        Assert.assertEquals(1, winner);

    }

    @Test
    public void player2Won() {
        //Given
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        gameLogicService.getPits()[1] = 5;

        //When
        final int winner = gameLogicService.retrieveWinner();

        //Then
        Assert.assertEquals(2, winner);

    }

    @Test
    public void initializePitsSuccessful() {
        //Given

        //When
        final GameLogicService gameLogicService = new GameLogicService(PLAYER1_NAME, PLAYER2_NAME);

        //Then
        Assert.assertEquals(0, gameLogicService.getPits()[0]);
        Assert.assertEquals(6, gameLogicService.getPits()[1]);
        Assert.assertEquals(0, gameLogicService.getPits()[7]);
        Assert.assertEquals(6, gameLogicService.getPits()[13]);

    }
}
