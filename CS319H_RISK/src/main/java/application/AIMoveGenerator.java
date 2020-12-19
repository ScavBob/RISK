package application;

import game.Game;
import managers.PlayerAction;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AIMoveGenerator {

    private static final int DEFAULT_DELAY_MS = 2000;


    public static void awaitAIAction(GameController controller, int phase, int level)
    {
        if (phase == GameController.REINFORCEMENT_PHASE)
            getReinforcementAction(controller, level);
        else if (phase == GameController.ATTACK_PHASE)
            getAttackAction(controller, level);
        else if (phase == GameController.FORTIFY_PHASE)
            getFortifyAction(controller, level);
        else
            System.err.println("Incorrect phase given to awaitAIAction()");

        //TODO
    }

    public static void getAttackAction(GameController controller, int level)
    {
        Player currentPlayer = Game.getInstance().getGameManager().getMatch().getCurrentPlayer();

        Region source = null;
        Region target = null;
        Map map = Game.getInstance().getGameManager().getMatch().getMap();
        boolean endPhase = true;
        int armyCount = 0;
        int difference = 0;
        for(Region r:currentPlayer.getRegions()){

            Region tempSource;
            Region tempTarget;

            ArrayList<Region> nList = map.getNeighbourOf(r);

            for(Region nR:nList){
                if(nR.getOwner() != currentPlayer){

                    tempSource = r;
                    tempTarget = nR;

                    if(source == null || target == null){
                        source = tempSource;
                        target = tempTarget;
                    }
                    else if((tempSource.getUnitCount()-tempTarget.getUnitCount()) > (source.getUnitCount()-target.getUnitCount())){
                        source = tempSource;
                        target = tempTarget;
                    }
                }
            }

        }
        difference = source.getUnitCount()-target.getUnitCount();

        if(source != null && target != null && (difference >= 4)){
            endPhase = false;
            armyCount = source.getUnitCount()-(1+difference/3);
        }

        PlayerAction playerAction = new PlayerAction(endPhase, GameController.ATTACK_PHASE, source, target, armyCount);
        sendDelayedAction(controller, DEFAULT_DELAY_MS, playerAction);

    }

    public static void getFortifyAction(GameController controller, int level)
    {

        Player currentPlayer = Game.getInstance().getGameManager().getMatch().getCurrentPlayer();
        Region source = null;
        Region target = null;
        Map map = Game.getInstance().getGameManager().getMatch().getMap();
        boolean endPhase = true;
        int armyCount = 0;

        for(Region r:currentPlayer.getRegions()){

            ArrayList<Region> nList = map.getNeighbourOf(r);
            boolean noEnemy = true;
            for(Region nR:nList){
                if(nR.getOwner() != currentPlayer){
                    noEnemy = false;
                    break;
                }
            }
            if(noEnemy && r.getUnitCount()>1){
                endPhase = false;
                if(source == null)
                    source = r;
                else if(source.getUnitCount() < r.getUnitCount())
                    source = r;
            }
        }

        if(endPhase){
            PlayerAction playerAction = new PlayerAction(endPhase, GameController.FORTIFY_PHASE, source, target, armyCount);
            sendDelayedAction(controller, DEFAULT_DELAY_MS, playerAction);
            return;
        }

        int difference = 0;
        int totalEnemyUnitCount = 0;
        for(Region r:currentPlayer.getRegions()){
            int tempDifference;
            int tempEnemyUnit = 0;

            ArrayList<Region> nList = map.getNeighbourOf(r);
            for(Region nR:nList){

                if(nR.getOwner() != currentPlayer){
                    tempEnemyUnit += nR.getUnitCount();
                    tempDifference = r.getUnitCount() - tempEnemyUnit;
                    if(!map.isConnected(source,r))
                        continue;
                    if (target == null){
                        target = r;
                        difference = tempDifference;
                    }
                    else if (difference >= tempDifference){
                        target = r;
                        difference = tempDifference;
                    }
                }
            }

        }

        armyCount = source.getUnitCount()-1;

        PlayerAction playerAction = new PlayerAction(endPhase, GameController.FORTIFY_PHASE, source, target, armyCount);
        sendDelayedAction(controller, DEFAULT_DELAY_MS, playerAction);

    }

    public static void getReinforcementAction(GameController controller, int level)
    {
        Player currentPlayer = Game.getInstance().getGameManager().getMatch().getCurrentPlayer();

        PlayerAction playerAction;

        if (currentPlayer.getAvailableReinforcements() == 0) {
            playerAction = new PlayerAction(true, GameController.REINFORCEMENT_PHASE, null, null, 0);
            sendDelayedAction(controller, DEFAULT_DELAY_MS, playerAction);
            return;
        }

        int randomCount = (int)(Math.random() * currentPlayer.getAvailableReinforcements()) + 1;
        List<Region> regions = currentPlayer.getRegions();
        int randomRegion = (int)(Math.random() * regions.size());
        Region chosenRegion = regions.get(randomRegion);

        playerAction = new PlayerAction(false, GameController.REINFORCEMENT_PHASE, chosenRegion, null, randomCount);
        sendDelayedAction(controller, DEFAULT_DELAY_MS, playerAction);
    }

    public static void sendDelayedAction(GameController controller, int delayMs, PlayerAction playerAction)
    {
        System.out.println("here " + playerAction);
        Timer timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.takePlayerAction(playerAction);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
