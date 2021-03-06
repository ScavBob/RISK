package screen.menu;

import application.Faction;
import application.Player;
import gamelauncher.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import manager.StorageManager;

import java.nio.file.Paths;
import java.util.ArrayList;

public class GameStartMenu extends Menu {

    private final Faction[] factions = {Faction.Ottoman, Faction.Germany, Faction.France, Faction.Britain, Faction.America, Faction.Italy, Faction.Russia};
    private String[] colors = {"Red", "Green", "Blue", "Yellow", "Purple", "Black", "Pink"};
    private int map;
    private Slot[] slots = new Slot[7];
    private boolean gameMode;
    private int AILevel;
    private int turnTime;

    private class Slot {
        private int x, y;
        private Group root;
        private boolean humanPlayer;
        private String playerName;
        private String playerColor;
        private boolean playerAdded;
        private int selectedFaction;

        private Button faction;
        private Button addPlayer;
        private Button color;
        private Button delete;
        private Button changeType;
        private Button type;
        private TextArea name;

        protected Slot(Group root, int i, String playerColor){
            addPlayer = new Button();
            playerAdded = false;
            color = new Button();
            delete = new Button();
            changeType = new Button();
            faction = new Button();
            type = new Button();
            name = new TextArea();
            this.playerColor = playerColor;
            playerName = "Player " + (i + 1);
            this.root = root;
            selectedFaction = i;
            x = 75;
            y = 60 + i*90;
            addButton(addPlayer, x-20, y, 495, 77, getClass().getResource("/GameStartMenu/AddPlayerButton.png").toExternalForm());
            addButton(type, x-20, y, 495, 77, getClass().getResource("/GameStartMenu/Type.png").toExternalForm());
            addButton(faction, x -10, y + 9, 60, 61, getClass().getResource("/GameResources/Factions/" + (i+1) + ".png").toExternalForm());
            addButton(color, x + 60, y + 25, 40, 26,  getClass().getResource("/GameResources/Colors/" + playerColor + ".png").toExternalForm());
            addButton(delete, x + 400, y + 13, 50, 50, getClass().getResource("/GameStartMenu/Delete.png").toExternalForm());
            addButton(changeType, x + 320, y + 8, 68, 60, getClass().getResource("/GameStartMenu/HUMAN.png").toExternalForm());
            name.setLayoutX(x + 110);
            name.setLayoutY(y + 13);
            changeType.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Image image;
                    if(humanPlayer) {
                        image = new Image(Paths.get(StorageManager.RESOURCES_FOLDER_NAME + "GameStartMenu\\AI.png").toUri().toString());
                        humanPlayer = false;
                    }
                    else{
                        image = new Image(Paths.get(StorageManager.RESOURCES_FOLDER_NAME + "GameStartMenu\\HUMAN.png").toUri().toString());
                        humanPlayer = true;
                    }
                    changeType.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                }
            });
            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removePlayer();
                }
            });
            name.setMaxSize(175, 50);
            name.setFont(new Font(15));
            name.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playerName = name.getText();
                }
            });
            draw(i);
        }

        private void addButton(Button button, int x, int y, int width, int height, String imagePath){
            Image image = new Image(imagePath);
            button.setMinSize(width, height);
            button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            button.setLayoutX(x);
            button.setLayoutY(y);
        }

        private void draw(int i){
                addPlayer.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    addPlayer();
                    humanPlayer = true;
                    name.setText(playerName);
                }
            });
                root.getChildren().add(addPlayer);
        }

        private void addPlayer() {
            root.getChildren().remove(addPlayer);
            root.getChildren().add(type);
            root.getChildren().add(delete);
            root.getChildren().add(faction);
            root.getChildren().add(color);
            root.getChildren().add(name);
            root.getChildren().add(changeType);
            playerAdded = true;
        }

        private void removePlayer(){
            root.getChildren().remove(type);
            root.getChildren().remove(delete);
            root.getChildren().remove(faction);
            root.getChildren().remove(color);
            root.getChildren().remove(name);
            root.getChildren().remove(changeType);
            root.getChildren().add(addPlayer);
            playerAdded = false;
        }

    }

    public GameStartMenu(int map) {
        this.gameMode = true;
        this.turnTime = 1;
        this.map = map;
    }

    @Override
    public Scene getScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720, Color.BLACK);
        Canvas canvas = new Canvas();
        setBackground(root, getClass().getResource("/Menu/Background.png").toExternalForm());
        drawRect("#b4c7e77f", 38, 38, 540, 654);
        root.getChildren().add(canvas);
        for(int i = 0; i < 7; i++) {
            slots[i] = new Slot(root, i, colors[i]);
        }
        drawImage(getClass().getResource("/GameStartMenu/MapPreview/" + map + "Preview.png").toExternalForm(), 600, 40);   //591x270
        drawImage(getClass().getResource("/GameStartMenu/MapPreview/MapPreview.png").toExternalForm(), 598, 38);   //594x273
        drawRect("#bfbfbf7f", 598, 331, 594, 361);
        drawRect("0000007f", 607, 341, 574, 341);
        drawImage(getClass().getResource("/GameStartMenu/GameRules/GameRules.png").toExternalForm(), 776, 346);
        drawImage(getClass().getResource("/GameStartMenu/GameRules/GameMode/GameMode.png").toExternalForm(), 836, 400);
        drawImage(getClass().getResource("/GameStartMenu/GameRules/AI/AIDifficulty.png").toExternalForm(), 836, 490);
        drawImage(getClass().getResource("/GameStartMenu/GameRules/Timer/TurnTimer.png").toExternalForm(), 836, 580);
        addButtons(root);
        return scene;
    }

    private void addButtons(Group root) {
        addButtons(root, "", 1220, 50, 55, 50, getClass().getResource("/GameStartMenu/Start.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
                Game.getInstance().getGameManager().getSoundManager().playClick();
            }
        });
        addButtons(root, "", 746, 430, 143, 50, getClass().getResource("/GameStartMenu/GameRules/GameMode/WorldDomination.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameMode = true;
                Game.getInstance().getGameManager().getSoundManager().playClick();
            }
        });
        addButtons(root, "", 899, 430, 143, 50, getClass().getResource("/GameStartMenu/GameRules/GameMode/SecretMission.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameMode = false;
                Game.getInstance().getGameManager().getSoundManager().playClick();
            }
        });
        addButtons(root, "", 823, 520, 143, 49, getClass().getResource("/GameStartMenu/GameRules/AI/2.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AILevel = 2;
            }
        });
        addButtons(root, "", 675, 520, 143, 49, getClass().getResource("/GameStartMenu/GameRules/AI/1.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AILevel = 1;
            }
        });
        addButtons(root, "", 971, 520, 143, 49, getClass().getResource("/GameStartMenu/GameRules/AI/3.png").toExternalForm(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AILevel = 3;
            }
        });
        int[] times = {100000, 1, 2, 3, 5, 10, 15, 30};
        for(int i = 0; i < times.length; i++){
            addButtons(root, (i == 0 ? "∞" : times[i] + ""), 679 + 53*i, 610, 50, 50, getClass().getResource("/GameStartMenu/GameRules/Timer/Time.png").toExternalForm(), event -> {
                if( ((Button)event.getSource()).getText() == "∞" )
                    turnTime = 100000;
                else
                    turnTime = Integer.parseInt(((Button)event.getSource()).getText());
            });
        }
    }

    private void startGame() {
        ArrayList<Player> playerList = new ArrayList<Player>();
        for (Slot s : slots) {
            if(s.playerAdded) {
                System.out.println(factions[s.selectedFaction]);
                playerList.add(new Player(s.playerName, s.playerColor, s.humanPlayer, factions[s.selectedFaction]));
            }
        }
        if (playerList.size() >= 2) {
            Game.getInstance().getGameManager().startMatch(map, playerList, turnTime*60, gameMode, AILevel);
        }
        else{
            //TODO
        }
    }
}
