package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {
    public static void main (String[] args) {
        launch (args);
    }

    private int numberOfPairs;
    private int cardsInOneRow;
    private Card selectedCard = null;
    private int clickCounter = 0;
    private int pairsFound = 0;
    private int time =0;

    @Override
    public void start (Stage primaryStage) {
        clickCounter=0;
        time=0;
        selectedCard=null;
        numberOfPairs=0;
        cardsInOneRow=0;
        pairsFound=0;
        primaryStage.setTitle ("Memory Puzzle");
        VBox vbox = new VBox ();
        vbox.setSpacing (15);
        vbox.setAlignment (Pos.CENTER);
        VBox text = new VBox ();
        text.setAlignment (Pos.CENTER);
        Label intro = new Label ("Witaj w grze Memory Puzzle!");
        intro.setFont (Font.font (16));
        Label diff = new Label ("Wybierz ilość kart: ");
        diff.setFont (Font.font (16));
        ObservableList<Integer> options = FXCollections.observableArrayList (16, 36, 64);
        ComboBox<Integer> difficulty = new ComboBox<> (options);
        difficulty.getSelectionModel ().selectFirst ();
        difficulty.setPrefWidth (55);
        HBox cardAmount = new HBox ();
        cardAmount.setAlignment (Pos.CENTER);
        cardAmount.getChildren ().addAll (diff,difficulty);
        text.getChildren ().addAll (intro, cardAmount);
        Button start = new Button ("Start");
        start.setPrefWidth (70);
        start.setOnAction (event -> {
            try {
                initialize (difficulty, primaryStage);
            } catch (URISyntaxException e) {
                e.printStackTrace ();
            }
        });
        vbox.getChildren ().addAll (text, start);
        vbox.setPrefSize (400, 200);
        vbox.setStyle("-fx-background-color: #A3A5F1;");
        primaryStage.setScene (new Scene (vbox));
        primaryStage.setResizable (false);
        primaryStage.show ();
    }

    public void initialize (ComboBox<Integer> difficulty, Stage stage) throws URISyntaxException {
        int difficultyInt = difficulty.getValue ();
        HBox content = new HBox ();
        content.setSpacing (30);
        VBox info = new VBox ();
        Button exit = new Button ("Powrót do menu");
        info.setAlignment (Pos.CENTER);
        info.setSpacing (200);
        exit.setOnAction (e -> start (stage));
        Label timerInfo = new Label ("Czas: ");
        timerInfo.setFont (Font.font (20));
        Label timer = new Label();
        timer.setFont (Font.font (20));
        long startTime = System.currentTimeMillis();
        new AnimationTimer () {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime ;
                time=(int)elapsedMillis/1000;
                timer.setText(Integer.toString (time));
            }
        }.start();
        VBox timerVBox = new VBox ();
        timerVBox.setAlignment (Pos.CENTER);
        timerVBox.getChildren ().addAll (timerInfo,timer);
        info.getChildren ().addAll (timerVBox,exit);
        GridPane gridpane = new GridPane ();
        gridpane.setHgap (1);
        gridpane.setVgap (1);
        this.numberOfPairs = difficultyInt / 2;
        this.cardsInOneRow = (int) Math.sqrt (difficultyInt);
        GridPane.setConstraints (gridpane, this.cardsInOneRow, difficultyInt / this.cardsInOneRow);
        List<Card> cards = new ArrayList<> ();
        for ( int i = 1; i <= numberOfPairs; i++ ) {
            String path = String.format ("%d.jpg",i);
            cards.add (new Card (path));
            cards.add (new Card (path));
        }
        Collections.shuffle (cards);
        int i = 0;
        int j = 0;
        for ( Card card : cards ) {
            card.setOnMouseClicked (e -> cardClicked (card, stage));
            if (i == this.cardsInOneRow) {
                j++;
                i = 0;
            }
            gridpane.add (card, i, j);
            i++;
        }
        content.setPadding (new Insets (30, 30, 30, 30));
        content.getChildren ().addAll (gridpane, info);
        content.setStyle("-fx-background-color: #A3A5F1;");
        stage.setTitle ("Memory Puzzle");
        stage.setScene (new Scene (content));
        stage.show ();
    }

    public void cardClicked (Card card, Stage stage) {
        if (!card.isClickable () || clickCounter == 2) {
            return;
        }
        clickCounter++;
        if (selectedCard == null) {
            selectedCard = card;
            card.shown ();
        } else {
            card.shown ();
                if (!card.getPath().equals(selectedCard.getPath())) {
                selectedCard.hidden ();
                card.hidden ();
                }
                else{
                    pairsFound++;
                    selectedCard.cardFound ();
                    card.cardFound ();
                }
                selectedCard=null;
                clickCounter=0;
            }
        if(pairsFound==numberOfPairs){
        gameWon (stage);
        }
    }
    public void gameWon(Stage stage){
        stage.setTitle ("Memory Puzzle");
        VBox vbox = new VBox ();
        vbox.setSpacing (10);
        vbox.setAlignment (Pos.CENTER);
        Label outro = new Label ("Udało ci się odnaleźć wszystkie pary!");
        outro.setFont (Font.font (16));
        String timeString = "Zajeło ci to "+time+" sekund";
        Label timeLabel = new Label (timeString);
        timeLabel.setFont (Font.font (16));
        Label playAgain = new Label ("Czy chcesz zagrać ponownie?");
        playAgain.setFont (Font.font (16));
        Button yes = new Button ("Tak");
        yes.setOnAction (e -> start (stage));
        Button no = new Button ("Nie");
        no.setOnAction (e->Platform.exit());
        HBox yesNo = new HBox ();
        yesNo.setAlignment (Pos.CENTER);
        yesNo.setSpacing (40);
        yesNo.getChildren ().addAll (yes,no);
        vbox.getChildren ().addAll (outro,timeLabel,playAgain,yesNo);
        vbox.setPrefSize (400, 150);
        vbox.setStyle("-fx-background-color: #A3A5F1;");
        stage.setScene (new Scene (vbox));
        stage.setResizable (false);
        stage.show ();
    }
}