package driver;

import java.io.IOException;
import java.util.ArrayList;

import board.Board;
import board.CardList;
import board.Displayable;
import board.Player;
import cards.Card;
import cards.CardType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GraphicalGame extends Application {
    private boolean p1plays = true;
	private Player currentPlayer;
    private static Player p1, p2;
    private ArrayList<Integer> indexMushrooms = new ArrayList<Integer>();

    private Stage stage;
    private HBox root;
    private VBox controlPane;
    private VBox activeCardArea;
    private VBox player1Area;
    private VBox player2Area;
    private HBox middleArea;
    private HBox forestArea = new HBox();
    private StackPane decayArea = new StackPane();
    private HBox middleLabels = new HBox();
    private HBox display1 = new HBox();
    private HBox hand1 = new HBox();
    private HBox display2 = new HBox();
    private HBox hand2 = new HBox();
    private Label score1 = new Label();
    private Label score2 = new Label();
    private Label stick1 = new Label();
    private Label stick2 = new Label();
    private Label status = new Label();
    private Label guide = new Label();
    private VBox hints = new VBox();
    private Button takeDecay = new Button("Take From Decay");
    private Button cookMushroom = new Button("Cook Mushrooms");
    private Button sellMushroom = new Button("Sell Mushrooms");
    private Button putPanDown = new Button("Put Pan Down");
    private Label cardPile = new Label();
    

    @Override
    public void start(Stage stage) throws IOException {
        takeDecay.setOnAction((t) -> {
            boolean succesfullMove = false;
            if (currentPlayer.takeFromDecay()) {
                succesfullMove = true;	
            } else {
                guide.setText("Hand Is Full.");
            }
            successMove(succesfullMove);
        });

        cookMushroom.setOnAction((t) -> {
            boolean succesfullMove = false;
            ArrayList<Card> cookingmushrooms = new ArrayList<Card>();
            for (Integer index : indexMushrooms) {
                cookingmushrooms.add(currentPlayer.getHand().getElementAt(index));
            }
            if (currentPlayer.cookMushrooms(cookingmushrooms)) {
                indexMushrooms.clear();
                succesfullMove=true;
            } else {
                guide.setText("Error In Selection.");
            }
            successMove(succesfullMove);
        });

        sellMushroom.setOnAction((t) -> {
            boolean succesfullMove = false;
            boolean flag = false;
            String mushName = new String();

            for (Integer index : indexMushrooms) {
                flag = false;
                String temp = currentPlayer.getHand().getElementAt(index).getName();
                if (mushName.isEmpty()) {
                    mushName = temp;
                }
                else if (mushName.equals(temp)) {
                    flag = true;
                } else {
                    break;
                }
            }

            if (flag) {
                if (currentPlayer.sellMushrooms(mushName, indexMushrooms.size())) {
                    indexMushrooms.clear();
                    succesfullMove=true;
                } else {
                    guide.setText("Error In Selection.");
                }
            }
            successMove(succesfullMove);
        });

        putPanDown.setOnAction((t) -> {
            boolean succesfullMove = false;
            if (currentPlayer.putPanDown()) {
                succesfullMove=true;
            } else {
                guide.setText("No Pan In Hand.");
            }
            successMove(succesfullMove);
        });

        forestArea.setOnMouseClicked(e -> {
            int index = forestArea.getChildren().indexOf(e.getTarget());
            if (index != -1) {
                forestEvent(index);
            }
        });

        decayArea.setOnMouseClicked(e -> {
            decayArea.getChildren().get(0).toFront();
        });

		gameRestart();
        displayGraphic();

        stage.setTitle("Game of Morels");
        stage.setScene(new Scene(root, 1380, 815));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void gameRestart() {
        Board.initialisePiles();
		Board.setUpCards();
		Board.getForestCardsPile().shufflePile();
	
		//Populate forest	
		for (int i=0; i<8;i++) {			
			Board.getForest().add(Board.getForestCardsPile().drawCard());		
		}
		//Initialise players and populate player hands
		p1  = new Player(); currentPlayer=p1; p2 = new Player();
		p1.addCardtoHand(Board.getForestCardsPile().drawCard());p1.addCardtoHand(Board.getForestCardsPile().drawCard());p1.addCardtoHand(Board.getForestCardsPile().drawCard());
		p2.addCardtoHand(Board.getForestCardsPile().drawCard());p2.addCardtoHand(Board.getForestCardsPile().drawCard());p2.addCardtoHand(Board.getForestCardsPile().drawCard());
    }

    private void successMove(boolean flag) {
        if (flag) {
            if (Board.getForest().size()>0) {
                Board.updateDecayPile();
            }
            if (Board.getForestCardsPile().pileSize()>0) {
                Board.getForest().add(Board.getForestCardsPile().drawCard());
            }

            if (p1plays) {
                updatePlayer1Display(p1.getDisplay());
                updatePlayer1Hand(p1.getHand());
                currentPlayer = p2;
                hand1.setStyle("-fx-cursor: none;");
                hand2.setStyle("-fx-cursor: hand;");
            } else {
                updatePlayer2Display(p2.getDisplay());
                updatePlayer2Hand(p2.getHand());
                currentPlayer = p1;
                hand1.setStyle("-fx-cursor: hand;");
                hand2.setStyle("-fx-cursor: none;");
            }
            p1plays = !p1plays;
            setGameStatus();
            updateForest(Board.getForest());
            updateDecay(Board.getDecayPile());
            indexMushrooms.clear();

            if (Board.getForest().size() == 0) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                Button restart = new Button("Restart");
                restart.setOnAction((t) -> {
                    gameRestart();
                    setGameStatus();
                    updateForest(Board.getForest());
                    updateDecay(Board.getDecayPile());
                    updatePlayer1Hand(p1.getHand());
                    updatePlayer1Display(p1.getDisplay());
                    updatePlayer2Hand(p2.getHand());
                    updatePlayer2Display(p2.getDisplay());
                    indexMushrooms.clear();
                    dialog.close();
                });
                VBox dialogVBox = new VBox(new Label("Player 1 : Player 2"), new Label(p1.getScore()+" : "+p2.getScore()));
                if (p1.getScore() > p2.getScore()) {
                    dialogVBox.getChildren().add(new Label("Player 1 Wins"));
                } else if (p1.getScore() < p2.getScore()) {
                    dialogVBox.getChildren().add(new Label("Player 2 Wins"));
                } else {
                    dialogVBox.getChildren().add(new Label("It is Draw"));
                }
                dialogVBox.getChildren().add(restart);
                dialogVBox.setAlignment(Pos.CENTER);
                dialogVBox.setSpacing(5);

                dialog.setScene(new Scene(dialogVBox, 300, 200));
                dialog.show();
            }
        }
    }

    private void forestEvent(int index) {
        boolean succesfullMove = false;
        if (currentPlayer.takeCardFromTheForest(Board.getForest().size()-index)) {
            if (Board.getForestCardsPile().pileSize()>0) {
                Board.getForest().add(Board.getForestCardsPile().drawCard());
            }
            succesfullMove = true; 		
        } else {
            guide.setText("Not Enough Sticks Or\nHand Is Full.");
        }
        successMove(succesfullMove);
    }

    private void displayGraphic() {
        cardPile.setPrefWidth(97);
        cardPile.setPrefHeight(164);
        cardPile.setStyle("-fx-border-color: black");
        cardPile.setAlignment(Pos.CENTER);

        forestArea.setPrefWidth(808);
        forestArea.setSpacing(10);
        forestArea.setStyle("-fx-cursor: hand;");

        decayArea.setStyle("-fx-cursor: hand;");
        HBox.setMargin(decayArea, new Insets(0, 0, 0, -30));

        setGameStatus();
        score1.setStyle("-fx-font-weight: bold");
        stick1.setStyle("-fx-font-weight: bold");
        score2.setStyle("-fx-font-weight: bold");
        stick2.setStyle("-fx-font-weight: bold");
        guide.setStyle("-fx-font-weight: bold");
        guide.setAlignment(Pos.CENTER);
        status.setStyle("-fx-font-weight: bold;"+"-fx-font-size: 18;"+"-fx-border-style: solid hidden solid hidden;"+"-fx-border-width: 2;");
        status.setPadding(new Insets(3));

        hints.setSpacing(2);
        hints.getChildren().add(new Label("  Hints Below"));
        hints.getChildren().add(new Label("- Action 1: Click card\nin Forest to take out\ncard."));
        hints.getChildren().add(new Label("- Action 2: Click Take\nFrom Decay button."));
        hints.getChildren().add(new Label("- Action 3: Select\nidentical mushrooms\n>= 3 and Click Cook."));
        hints.getChildren().add(new Label("- Action 4: Select\nidentical mushrooms\n>= 2 and Click Sell."));
        hints.getChildren().add(new Label("- Action 5: Click Put\nPan Down."));
        hints.getChildren().add(new Label("- Night Card have blue\nborder and worth 2\nmushrooms"));
        hints.getChildren().add(new Label("- Day Card worth 1\nmushroom"));
        hints.getChildren().add(new Label("- Click Decay Pile to see\ndifferent cards in the\npile."));
        hints.setStyle("-fx-border-color: black");

        cookMushroom.setStyle("-fx-text-fill: rgb(112, 23, 4);"+"-fx-border-color: rgb(168, 80, 50);"
                            +"-fx-border-radius: 5;"+"-fx-background-color: rgb(181, 122, 110);"+"-fx-cursor: hand;");
        sellMushroom.setStyle("-fx-text-fill: rgb(117, 74, 25);"+"-fx-border-color: rgb(117, 74, 25);"
                            +"-fx-border-radius: 5;"+"-fx-background-color: rgb(227, 178, 104);"+"-fx-cursor: hand;");
        putPanDown.setStyle("-fx-text-fill: rgb(28, 22, 130);"+"-fx-border-color: rgb(28, 22, 130);"
                            +"-fx-border-radius: 5;"+"-fx-background-color: rgb(118, 149, 196);"+"-fx-cursor: hand;"); 
        takeDecay.setStyle("-fx-text-fill: rgb(20, 87, 17);"+"-fx-border-color: rgb(20, 87, 17);"
                            +"-fx-border-radius: 5;"+"-fx-background-color: rgb(121, 196, 118);"+"-fx-cursor: hand;");    

        controlPane = new VBox(score1, stick1, status, guide, hints, cookMushroom, sellMushroom, putPanDown, takeDecay, score2, stick2);
        controlPane.setStyle("-fx-border-style: hidden hidden hidden solid;"+"-fx-border-width: 3;"
                            +"-fx-border-color: black;"+"-fx-background-color: #D2B48C;");
        controlPane.setPrefWidth(300);
        controlPane.setSpacing(10);
        controlPane.setAlignment(Pos.TOP_CENTER);
        controlPane.setPadding(new Insets(5));
        VBox.setMargin(score1, new Insets(20, 0, 0, 0));
        VBox.setMargin(status, new Insets(25, 0, 0, 0));
        VBox.setMargin(score2, new Insets(20, 0, 0, 0));
           
        updateForest(Board.getForest());
        middleArea = new HBox(cardPile, forestArea, decayArea);
        middleArea.setPrefHeight(164);
        middleArea.setSpacing(10);
        middleArea.setPadding(new Insets(5, 0, 0, 15));

        for (int i = 6; i > 0; i--) {
            middleLabels.getChildren().add(new Label("Sticks " + i));
            middleLabels.getChildren().get(6-i).setStyle("-fx-font-weight: bold;");
        }
        middleLabels.getChildren().add(new Label("On feets"));
        middleLabels.getChildren().add(new Label("On feets"));
        middleLabels.getChildren().add(new Label("Decay Area"));

        middleLabels.getChildren().get(6).setStyle("-fx-font-weight: bold;");
        middleLabels.getChildren().get(7).setStyle("-fx-font-weight: bold;");
        middleLabels.getChildren().get(8).setStyle("-fx-font-weight: bold;");
        
        HBox.setMargin(middleLabels.getChildren().get(0), new Insets(0, 0, 0, 100));

        for (int i = 1; i < 7; i++) {
            HBox.setMargin(middleLabels.getChildren().get(i), new Insets(0, 0, 0, 10));
        }

        HBox.setMargin(middleLabels.getChildren().get(8), new Insets(0, 0, 0, 60));

        middleLabels.setSpacing(40);
        middleLabels.setPadding(new Insets(3, 0, 0, 45));

        hand1.setSpacing(4);
        hand1.setStyle("-fx-cursor: hand;");
        hand1.setMinHeight(164);
        display1.setStyle("-fx-border-color: orange;"+"-fx-border-width: 3;"+"-fx-border-style: segments(10, 15, 15, 15) line-cap round;");
        display1.setMinHeight(120);
        display1.setSpacing(20);
        VBox.setMargin(display1, new Insets(0, 20, 0, 20));

        player1Area = new VBox(hand1, display1);
        player1Area.setPrefHeight(302);
        player1Area.setPadding(new Insets(5));
        player1Area.setSpacing(5);
        updatePlayer1Hand(p1.getHand());
        updatePlayer1Display(p1.getDisplay());

        hand2.setSpacing(4);
        hand2.setMinHeight(164);
        display2.setStyle("-fx-border-color: orange;"+"-fx-border-width: 3;"+"-fx-border-style: segments(10, 15, 15, 15) line-cap round;");
        display2.setMinHeight(120);
        display2.setSpacing(20);
        VBox.setMargin(display2, new Insets(0, 20, 5, 20));
        
        player2Area = new VBox(display2, hand2);
        player2Area.setPrefHeight(302);
        player2Area.setPadding(new Insets(5));
        player1Area.setSpacing(5);
        updatePlayer2Hand(p2.getHand());
        updatePlayer2Display(p2.getDisplay());

        activeCardArea = new VBox(player1Area, middleArea, middleLabels, player2Area);
        activeCardArea.setPrefWidth(1300);

        root = new HBox(activeCardArea, controlPane);
        root.setBackground(new Background(new BackgroundFill(Color.web("#339966"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setGameStatus() {
        cardPile.setText("Forest Cards\nRemains "+Board.getForestCardsPile().pileSize());
        
        score1.setText("Player 1 Scores: "+p1.getScore());
        stick1.setText("Player 1 Sticks: "+p1.getStickNumber());
        score2.setText("Player 2 Scores: "+p2.getScore());
        stick2.setText("Player 2 Sticks: "+p2.getStickNumber());

        if (p1plays) {
            status.setText("Player 1's Turn");
            status.setTextFill(Color.web("red"));
        } else {
            status.setText("Player 2's Turn");
            status.setTextFill(Color.web("blue"));
        }
        guide.setText("Take One Of The\n5 Actions.");
    }

    private void updateForest(CardList cl) {
        forestArea.getChildren().clear();
        
        for (int i=0; i < cl.size(); i++) {
            if (cl.getElementAt(i).getType().equals(CardType.NIGHTMUSHROOM)) {
                StackPane cardHold = new StackPane(new ImageView(new Image("file:img/"+cl.getElementAt(i).getName()+".jpg")));
                cardHold.getChildren().get(0).setOnMouseClicked(e -> {
                    forestEvent(forestArea.getChildren().indexOf(cardHold.getChildren().get(0).getParent()));
                });
                cardHold.setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                forestArea.getChildren().add(cardHold);
            } else {
                forestArea.getChildren().add(new ImageView(new Image("file:img/"+cl.getElementAt(i).getName()+".jpg")));
            }
		}
        forestArea.setPadding(new Insets(0, 0, 0, (8-cl.size())*108));
    }

    private void updatePlayer1Hand(Displayable d) {
        hand1.getChildren().clear();

        for (int i = 0; i < d.size(); i++) {
            StackPane cardHold = new StackPane(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg")));

            cardHold.getChildren().get(0).setOnMouseClicked(e -> {
                if (p1plays) {
                    int index = hand1.getChildren().indexOf(cardHold.getChildren().get(0).getParent());
                    if (indexMushrooms.contains(index)) {
                        indexMushrooms.remove(indexMushrooms.indexOf(index));
                        if (currentPlayer.getHand().getElementAt(index).getType().equals(CardType.NIGHTMUSHROOM)) {
                            hand1.getChildren().get(index).setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                        } else {
                            hand1.getChildren().get(index).setStyle("-fx-border-color: transparent;"+"-fx-border-width: 0;");
                        }
                    } else {
                        indexMushrooms.add(index);
                        hand1.getChildren().get(index).setStyle("-fx-border-color: yellow;"+"-fx-border-width: 3;");
                    }
                }
            });

            if (d.getElementAt(i).getType().equals(CardType.NIGHTMUSHROOM)) {
                cardHold.setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                hand1.getChildren().add(cardHold);
            } else {
                hand1.getChildren().add(cardHold);
            }
        }
    }

    private void updatePlayer2Hand(Displayable d) {
        hand2.getChildren().clear();
        
        for (int i = 0; i < d.size(); i++) {
            StackPane cardHold = new StackPane(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg")));

            cardHold.getChildren().get(0).setOnMouseClicked(e -> {
                if (!p1plays) {
                    int index = hand2.getChildren().indexOf(cardHold.getChildren().get(0).getParent());
                    if (indexMushrooms.contains(index)) {
                        indexMushrooms.remove(indexMushrooms.indexOf(index));
                        if (currentPlayer.getHand().getElementAt(index).getType().equals(CardType.NIGHTMUSHROOM)) {
                            hand2.getChildren().get(index).setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                        } else {
                            hand2.getChildren().get(index).setStyle("-fx-border-color: transparent;"+"-fx-border-width: 0;");
                        }
                    } else {
                        indexMushrooms.add(index);
                        hand2.getChildren().get(index).setStyle("-fx-border-color: yellow;"+"-fx-border-width: 3;");
                    }
                }
            });

            if (d.getElementAt(i).getType().equals(CardType.NIGHTMUSHROOM)) {
                cardHold.setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                hand2.getChildren().add(cardHold);
            } else {
                hand2.getChildren().add(cardHold);
            }
        }
    }

    private void updatePlayer1Display(Displayable d) {
        boolean stickAdded = false;
        display1.getChildren().clear();
        
        for (int i = 0; i < d.size(); i++) {
            if (!stickAdded && d.getElementAt(i).getName() == "stick") {
                StackPane cardHold = new StackPane(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg", 67, 120, false, false)));
                Label stickAmount = new Label("X "+currentPlayer.getStickNumber());
                stickAmount.setStyle("-fx-background-color: white;"+"-fx-border-width: 2;"+"-fx-border-color: black;"+"-fx-font-weight: bold;");
                cardHold.getChildren().add(stickAmount);
                display1.getChildren().add(cardHold);
                stickAdded = true;
            } else if (d.getElementAt(i).getName() != "stick") {
                display1.getChildren().add(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg", 67, 120, false, false)));
            }
        }
    }

    private void updatePlayer2Display(Displayable d) {
        boolean stickAdded = false;
        display2.getChildren().clear();
        
        for (int i = 0; i < d.size(); i++) {
            if (!stickAdded && d.getElementAt(i).getName() == "stick") {
                StackPane cardHold = new StackPane(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg", 67, 120, false, false)));
                Label stickAmount = new Label("X "+currentPlayer.getStickNumber());
                stickAmount.setStyle("-fx-background-color: white;"+"-fx-border-width: 2;"+"-fx-border-color: black;"+"-fx-font-weight: bold;");
                cardHold.getChildren().add(stickAmount);
                display2.getChildren().add(cardHold);
                stickAdded = true;
            } else if (d.getElementAt(i).getName() != "stick") {
                display2.getChildren().add(new ImageView(new Image("file:img/"+d.getElementAt(i).getName()+".jpg", 67, 120, false, false)));
            }
        }
    }

    private void updateDecay(ArrayList<Card> decayPile) {
        decayArea.getChildren().clear();

        for (Card card : decayPile) {
            if (card.getType().equals(CardType.NIGHTMUSHROOM)) {
                VBox cardHold = new VBox(new ImageView(new Image("file:img/"+card.getName()+".jpg")));
                cardHold.setStyle("-fx-border-color: darkblue;"+"-fx-border-width: 3;");
                decayArea.getChildren().add(cardHold);
            } else {
                decayArea.getChildren().add(new ImageView(new Image("file:img/"+card.getName()+".jpg")));
            }
        }

        for (int i = 1; i < decayArea.getChildren().size(); i++) {
            StackPane.setMargin(decayArea.getChildren().get(i), new Insets(0, 0, 0, i*30));
        }

        HBox.setMargin(decayArea, new Insets(0, 0, 0, 50-decayArea.getChildren().size()*15));
    }
}
