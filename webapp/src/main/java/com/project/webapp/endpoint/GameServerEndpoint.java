package com.project.webapp.endpoint;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.project.webapp.model.ResponseMessage;
import com.project.webapp.model.SendMessage;
import com.project.webapp.service.GameLogicService;
import org.slf4j.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Server Endpoint.
 * Created by LaMarinescu on 10/23/2016.
 */
@ServerEndpoint("/websocket")
public class GameServerEndpoint {

    private static final Logger LOG = getLogger(GameServerEndpoint.class);
    private static final Set<Session> clients = Collections.synchronizedSet(Sets.newHashSetWithExpectedSize(2));
    private static final String INVALID = "-1";

    //TODO refactor this
    private static Optional<String> player1 = Optional.empty();
    private static Optional<String> player2 = Optional.empty();
    private static Optional<GameLogicService> gameLogicService = Optional.empty();

    @OnOpen
    public void onOpen(final Session session) {
        LOG.info("On open");
        clients.add(session);
    }

    @OnMessage
    public void onMessage(final String message, final Session session) throws IOException {
        synchronized (clients) {

            LOG.debug("On message. No. of clients: " + clients.size());

            final Gson gson = new Gson();
            final ResponseMessage parsedResponseMessage = gson.fromJson(message, ResponseMessage.class);
            final String currentPlayer = parsedResponseMessage.getName();

            // check if user started the game
            if (INVALID.equals(parsedResponseMessage.getId())) {
                final Optional<String> name = Optional.of(currentPlayer);
                // check if both players are present in order to start the game
                if (!player1.isPresent()) {
                    player1 = name;

                    if (player2.isPresent()) {
                        initializeGame(session);
                    } else {
                        sendToClient("", false, false);
                    }
                } else {
                    player2 = name;
                    if (!gameLogicService.isPresent()) {
                        // init game
                        initializeGame(session);
                    }
                }
            } else {

                LOG.debug("Send to the client.");

                final int selectedId = Integer.parseInt(parsedResponseMessage.getId());
                final int selectedValue = Integer.parseInt(parsedResponseMessage.getValue());

                LOG.debug("Current pit:  " + selectedId);
                LOG.debug("Value of the current pit: " + selectedValue);

                if (gameLogicService.isPresent() && player1.isPresent() && player2.isPresent()) {
                    final GameLogicService gameService = GameServerEndpoint.gameLogicService.get();
                    final boolean isTurn = gameService.turn(currentPlayer, selectedId, selectedValue);
                    boolean isOver = gameService.isOver();
                    if (isOver) {
                        LOG.debug("It's over");
                        final int winner = gameService.retrieveWinner();
                        if (winner == 1) {
                            LOG.debug("Player1 won");
                            sendToClient(player1.get(), true, true);
                        } else if (winner == 2) {
                            LOG.debug("Player2 won");
                            sendToClient(player2.get(), true, true);
                        } else {
                            LOG.debug("tie");
                            sendToClient("", true, true);
                        }
                    } else if (isTurn) {
                        LOG.debug("Same turn");
                        sendToClient(currentPlayer, true, false);
                    } else {
                        LOG.debug("Oponent turn");
                        sendToClient(player1.get().equals(currentPlayer) ? player2.get() : player1.get(), true, false);
                    }
                }

            }

        }

    }

    @OnClose
    public void onClose(final Session session) {
        LOG.debug("On close");
        clients.remove(session);
        //initializePits();
        player1 = Optional.empty();
        player2 = Optional.empty();
        if (clients.size() > 0) {
            clients.forEach(client -> sendToClient("", false, false));
        }
    }

    /**
     * Send the message from the server to the client as a JSON.
     *
     * @param name       the player name
     * @param hasStarted true, if game has started
     * @param isOver     true, if game is over
     */
    private void sendToClient(final String name, final boolean hasStarted, final boolean isOver) {
        final Gson gson = new Gson();

        final SendMessage sendMessage = new SendMessage(name, hasStarted, isOver,
                !gameLogicService.isPresent() ? new int[14] : gameLogicService.get().getPits());
        clients.forEach(openedSession -> {
            try {
                openedSession.getBasicRemote().sendText(gson.toJson(sendMessage));
            } catch (IOException e) {
                LOG.error("Sending message operation to the client failed. Reason: " + e.getMessage(), e);
            }
        });
    }


    /**
     * Initialize the game.
     *
     * @param session the {@link Session}
     * @throws IOException the {@link IOException}
     */
    private void initializeGame(final Session session) throws IOException {

        if (player1.isPresent() && player2.isPresent()) {
            LOG.info("Initialize Game");
            gameLogicService = Optional.of(new GameLogicService(player1.get(), player2.get()));
            sendToClient(player1.get(), true, false);
        }
    }
}
