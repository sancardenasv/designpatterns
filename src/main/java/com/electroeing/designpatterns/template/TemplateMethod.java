package com.electroeing.designpatterns.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    protected int currentPLayer;
    protected final int numberOfPlayers;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void run() {
        start();
        while (!haveWinner()) {
            takeTurn();
        }

        logger.info("Player {} wins!", getWinningPlayer());
    }

    protected abstract int getWinningPlayer();
    protected abstract void takeTurn();
    protected abstract boolean haveWinner();
    protected abstract void start();
}

class Chess extends Game {
    private static final Logger logger = LoggerFactory.getLogger(Chess.class);

    private int maxTurns = 10;
    private int turn = 1;

    public Chess() {
        super(2);
    }

    @Override
    protected int getWinningPlayer() {
        return 0;
    }

    @Override
    protected void takeTurn() {
        currentPLayer = (currentPLayer + 1) % numberOfPlayers;
        logger.info("Turn {} taken by player {}", turn++, currentPLayer);
    }

    @Override
    protected boolean haveWinner() {
        return turn == maxTurns;
    }

    @Override
    protected void start() {
        logger.info("Starting a game of chess");
    }
}

public class TemplateMethod {
    public static void main(String[] args) {
        new Chess().run();
    }
}
