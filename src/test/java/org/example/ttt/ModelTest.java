package org.example.ttt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {
    Model model = new Model();

    @Test
    void gameIsOverWithFullBoardTest() {
        int boardLength = model.getBoard().length;
        for (int i = 0; i < boardLength; i++) {
            model.makeMove(i);
        }
        assertTrue(model.checkIfGameIsOver());//fastställer att spelet är alltid över om brädan är full
    }

}