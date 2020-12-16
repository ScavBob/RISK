package application;

import java.lang.Thread.State;

import game.Game;

public class GameController {

    //maxTurnTime is seconds
    private int maxTurnTime;
    // Where should we checked the maxTurnTime and the actual game time. Also we can use AnimationTimer class of java instead of the timer class.
    private AnimationTimer turnTimer;
    private BattleManager battleManager;
    private StateManager stateManager;

    // Timer limits each player. It must be given as a attribute to the constructor.
    public GameController(int maxTurnTime, BattleManager battleManager,StateManager stateManager){
        this.turnTimer = new Timer();
        this.maxTurnTime = maxTurnTime;
        this. battleManager = battleManager;
        this.stateManager = stateManager;
    }
    public void startRound(){
        turnTimer.start();
    }
    public void endTurn(){
        turnTimer.reset();;
    }
    public void battle(){
        turnTimer.stop();
    }
}
