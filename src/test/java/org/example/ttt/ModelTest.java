package org.example.ttt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private Model model;
    private String X = "X";
    private String O = "O";


    @BeforeEach //innan varje test
    @DisplayName("Set up for a new model")
    void setUp() {
        model = new Model(); // gör en ny instans av Model för board ska va tom för att kunna testas på
    }

    @Test
    @DisplayName("Testing valid moves.")
    void testMakeValidMove() {
        // Gör ett drag på pos 0 och kolla om det är giltigt
        model.move(0, X); //lägg X på pos 0 vilket är första rutan
        assertEquals(X, model.getBoard()[0], "Expected X to be placed at position 0.");
    }

    @Test
    @DisplayName("Testing for denying moves")
    void testDisallowLegalMove() {
        // Gör ett drag på pos 0 med X och kolla om det är giltigt. Gör liknande drag som tidigare test men försöker lägga O på pos 0 också.
        model.move(0, X);
        assertEquals(X, model.getBoard()[0], "Expected X to be placed at position 0.");
        model.move(0, O);
        assertEquals(X, model.getBoard()[0], "Expected X to be placed at position 0.");//kolla igen
    }

    @Test
    @DisplayName("Testing invalid moves")
    void testMakeInvalidMove() {
        // Gör ett drag på pos 0 med O och sen försöka lägga X på pos 0 också.
        model.move(0, O);
        assertFalse(model.makeMove(0), "Expected move to be invalid when cell is already taken.");
        assertNotEquals(X, model.getBoard()[0], "Expected cell to still contain X.");
    }


    @Test
    @DisplayName("Testing winning conditions for X")
    void testWinConditionForX() {
        // Gör drag så att X vinner genom vinst kombinationer, börjar med X och sedan är det Os tur
        model.move(0, X);
        model.move(3, O);
        model.move(1, X);
        model.move(4, O);
        //assertFalse(model.checkIfGameIsOver(), "Expected game not to be over yet.");
        model.move(2, X);

        assertTrue(model.checkIfGameIsOver(), "Expected game to be over with X as the winner.");
        assertEquals(1, model.getXScore(), "Expected X's score to be 1 after winning.");
        assertEquals(X, model.getBoard()[0], "Expected X in winning position.");
    }

    @Test
    @DisplayName("Testing winning conditions for O")
    void testWinConditionForO() {
        // Gör drag så att O vinner
        model.move(0, X);
        model.move(3, O);
        model.move(1, X);
        model.move(4, O);
        model.move(6, X);
        model.move(5, O);

        assertTrue(model.checkIfGameIsOver(), "Expected game to be over with O as the winner.");
        assertEquals(1, model.getOScore(), "Expected O's score to be 1 after winning.");
    }

    @Test
    @DisplayName("Testing draw condition")
    void testDrawCondition() {
        // Gör drag för ett oavgjort spel
        model.move(0, X);
        model.move(1, O);
        model.move(2, X);
        model.move(4, O);
        model.move(3, X);
        model.move(5, O);
        model.move(7, X);
        model.move(6, O);
        model.move(8, X);

        assertTrue(model.checkIfGameIsOver());
    }
}
