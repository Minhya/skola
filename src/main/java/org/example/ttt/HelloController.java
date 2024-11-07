package org.example.ttt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HelloController implements Initializable, PropertyChangeListener { //har hand om UI och integrerar med model för att uppdatera displayen baserat på hur spelet går

    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9; //knapparna eller rutorna för brädan. I UI

    @FXML
    private Text winnerText, xScoreText, oScoreText;

    private Model model;
    private ArrayList<Button> buttons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {//initialiserar model
        model = new Model();
        model.addPropertyChangeListener(this);//initialiserar lyssnaren, övervakar events och "reagerar". ändringar i spel logiken (model) kommer att notifiera hellocontroller om nuvarande spelare och vinnare

        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));
        setupButtons(); //sätter actions till knapparna
        updateScores(); // visar poäng
    }

    private void setupButtons() {//sätter upp knapparna för att bli tryckta på med handleButtonClick
        for (int i = 0; i < buttons.size(); i++) {
            final int index = i;
            Button button = buttons.get(i);
            button.setOnAction(e -> handleButtonClick(index));
        }
    }

    private void handleButtonClick(int index) {
        if (model.makeMove(index)) {//gör drag om det är tillgängligt genom model
            if (model.isGameOver()) {//kollar om spelet är över genom model
                disableAllButtons();//om spelet är över så ska alla knappar resettas och bli tillgängligt igen
            }
        }
    }

    private void disableAllButtons() {
        buttons.forEach(button -> button.setDisable(true)); //är till för när spelet resettas och knapparna ska bli tillgängliga igen
    }

    @FXML
    void restartGame() {
        model.resetGame();//återställer model och spelets "tillstånd"
        buttons.forEach(button -> { //återställer knapparna
            button.setText("");
            button.setDisable(false);
        });
        winnerText.setText("Tic-Tac-Toe");//återställer texten till TTT
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) { //uppdaterar UI baserat på ändringar
        switch (evt.getPropertyName()) {
            case "board" -> updateBoard((String[]) evt.getNewValue());
            case "winner" -> winnerText.setText((String) evt.getNewValue());
            case "score" -> updateScores();
        }
    }

    private void updateBoard(String[] board) { //uppdaterar så att knapp text matchar spelets tillstånd
        for (int i = 0; i < board.length; i++) {
            buttons.get(i).setText(board[i] != null ? board[i] : "");
        }
    }

    private void updateScores() { //uppdaterar poängen texten via poängen den får från model
        xScoreText.setText("X Score: " + model.getXScore());
        oScoreText.setText("O Score: " + model.getOScore());
    }
}
