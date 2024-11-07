package org.example.ttt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model { //spel logik och data för TTT. har hand om brädan, spelarens drag och status på spelet som poäng och vinst.
    private String[] board; //Array för lagra hur boarden är. Om det är X eller O eller tomt.
    private String currentPlayer; //lagrar nuvarande spelar, om det är X eller O
    private int xScore; // poäng för X spelare
    private int oScore; // poäng för O.
    private final PropertyChangeSupport support; //Hanterar förändringar för att uppdatera UI
    private final BotenAnna botenAnna; //Bot instansen, spelar O rollen.
    private final Random random; //slumpmässigt, för att välja drag

    public Model() {
        //Konstruktor för initialisering av spel board^ och poäng. Första spelaren blir X.
        board = new String[9];
        currentPlayer = "X";  // Väljer X som första spelare.
        xScore = 0;
        oScore = 0;
        support = new PropertyChangeSupport(this); // initialisering
        botenAnna = new BotenAnna(); // Instansering av bot
        random = new Random(); // instansiering av slumpmässiga drag.
    }

    public String[] getBoard() { //getter för board
        return board;
    }

    public String getCurrentPlayer() { //getter för nuvarande spelare.
        return currentPlayer;
    }

    public int getXScore() { //getter för poäng för X
        return xScore;
    }

    public int getOScore() { //Getter för poäng for O, boten
        return oScore;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) { //Lyssnare för att uppdatera UI kompnenter när något ändrats. Data ändrats.
        support.addPropertyChangeListener(listener);
    }

    public boolean makeMove(int index) { //metod för att placera nuvarande spelarens markering/bricka på brädan
        if (board[index] == null) { //kollar om någon ruta är tom
            board[index] = currentPlayer;//placerar "bricka"
            support.firePropertyChange("board", null, board);//säger till UI om förändring av brädan
            if (!checkIfGameIsOver()) {//kollar om spelet är över
                togglePlayer();//nästa spelares tur, byter spelare
                if ("O".equals(currentPlayer)) { //om det är Os tur så gör boten ett drag
                    makeBotMove(); // botens drag
                }
            }
            return true;
        }
        return false; //ger falsk om rutan är tagen, blir ogiltig då
    }

    private void togglePlayer() { // byter mellan spelare
        currentPlayer = currentPlayer.equals("X") ? "O" : "X"; // om nuvarande spelare är X
        support.firePropertyChange("currentPlayer", null, currentPlayer); //säger till UI att om spelarbyte
    }

    private void makeBotMove() { //botens drag, slumpmässiga drag om tillgängligt
        int botMove = botenAnna.pickRandomMove();
        if (botMove != -1) {
            makeMove(botMove); // Botens drag
        }
    }

    public boolean checkIfGameIsOver() {//kollar om spelet är över, om någon har vunnit eller om det är oavgjort
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {//kombinationerna för vinst
                case 0 -> board[0] + board[1] + board[2];
                case 1 -> board[3] + board[4] + board[5];
                case 2 -> board[6] + board[7] + board[8];
                case 3 -> board[0] + board[4] + board[8];
                case 4 -> board[2] + board[4] + board[6];
                case 5 -> board[0] + board[3] + board[6];
                case 6 -> board[1] + board[4] + board[7];
                case 7 -> board[2] + board[5] + board[8];
                default -> null;
            };

            if ("XXX".equals(line)) {//XXX ger vinst till X
                xScore++;//lägger till poäng för X
                support.firePropertyChange("winner", null, "X won!");//uppdaterar UI och skriver ut att X har vunnit
                support.firePropertyChange("score", null, new int[]{xScore, oScore});//uppdaterar poäng i UI
                return true;
            } else if ("OOO".equals(line)) {//när O vinner.
                oScore++; //poäng till O
                support.firePropertyChange("winner", null, "O won!");// text om vinst
                support.firePropertyChange("score", null, new int[]{xScore, oScore}); //uppdaterar poäng i UI
                return true;
            }
        }

        // kollar om det är oavgjort
        boolean isDraw = true;
        for (String cell : board) {
            if (cell == null) {//om rutan är tom är det inte oavgjort ännu
                isDraw = false;
                break;
            }
        }
        if (isDraw) {// om det är oavgjort, gör detta:
            support.firePropertyChange("winner", null, "It's a draw!");//ändrar UI till att det är oavgjort
            return true;
        }

        return false; //fortsätter spelet om det inte finns någon vinnare eller om det inte är oavgjort ännu.
    }

    public void resetGame() {//startar om spelet.
        board = new String[9];//nya tomma rutor
        currentPlayer = "X"; //sätter första spelaren till X
        support.firePropertyChange("board", null, board);//säger till UI att brädan har resettats.
        support.firePropertyChange("currentPlayer", null, currentPlayer);//säger till UI spelarna har resettats.
    }

    // Bot class for random moves
    private class BotenAnna { //bot klassen
        public int pickRandomMove() { //väljer drag som är slumpmässiga
            List<Integer> availableMoves = new ArrayList<>();
            for (int i = 0; i < board.length; i++) {
                if (board[i] == null) {//lägger till tillgängliga rutor till listan för att boten ska kunna lägga brickorna på dom
                    availableMoves.add(i);
                }
            }
            if (!availableMoves.isEmpty()) {//väljer tillgängliga men slumpmässiga drag
                return availableMoves.get(random.nextInt(availableMoves.size()));
            }
            return -1; // indikerar att det inte finns tillgängliga drag
        }
    }
}
