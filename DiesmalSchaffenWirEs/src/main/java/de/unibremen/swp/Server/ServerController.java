package de.unibremen.swp.Server;

import de.unibremen.swp.Game.Components.Cards.*;
import de.unibremen.swp.Game.Components.Cards.Type.PathType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureCardType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureType;
import de.unibremen.swp.Game.Components.Field;
import java.util.*;
import java.util.List;

/**
 * Server controller
 * Defines necessary methods to construct
 * game objects for GameClient reception
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class ServerController {

    /**
     * Creates a randomized playing field, adhering to written
     * game rules about specified PathCards and layout
     * @return A randomized playing field
     */
    public Field createField(){
        PathCard extraCard;
        Queue<PathCard> queue = new LinkedList<>();

        queue.add(new PathCard(0, PathType.ANGLE, "/p_L_startred.jpg"));
        queue.add(new PathCard(3, PathType.JUNCTION, TreasureType.KEY));
        queue.add(new PathCard(3, PathType.JUNCTION, TreasureType.MAP));
        queue.add(new PathCard(3, PathType.ANGLE, TreasureType.NONE));

        queue.add(new PathCard(0, PathType.JUNCTION, TreasureType.POTIONY));
        queue.add(new PathCard(3, PathType.JUNCTION, TreasureType.GEAR));
        queue.add(new PathCard(2, PathType.JUNCTION, TreasureType.EYE));
        queue.add(new PathCard(2, PathType.JUNCTION, TreasureType.SKULL));

        queue.add(new PathCard(0, PathType.JUNCTION, TreasureType.HEART));
        queue.add(new PathCard(0, PathType.JUNCTION, TreasureType.POTIONB));
        queue.add(new PathCard(1, PathType.JUNCTION, TreasureType.BONE));
        queue.add(new PathCard(2, PathType.JUNCTION, TreasureType.PAPER));

        queue.add(new PathCard(1, PathType.ANGLE, TreasureType.NONE));
        queue.add(new PathCard(1, PathType.JUNCTION,TreasureType.CANDLE));
        queue.add(new PathCard(1, PathType.JUNCTION, TreasureType.BOOK));
        queue.add(new PathCard(2, PathType.ANGLE, "/p_L_startgreen.jpg"));


        Random random = new Random();
        Queue<PathCard> queueMoveable = new LinkedList<>();

        for(int i = 0; i < 12; i++) {
            queueMoveable.add(new PathCard(random.nextInt(4), PathType.STRAIGHT, TreasureType.NONE));
        }

        for(int i = 0; i < 6; i++) {
            queueMoveable.add(new PathCard(random.nextInt(4), PathType.JUNCTION, TreasureType.junctionTypes[i]));
        }

        for(int i = 0; i < 6; i++ ) {
            queueMoveable.add(new PathCard(random.nextInt(4), PathType.ANGLE, TreasureType.angleTypes[i]));
        }

        for(int i = 0; i < 10; i++) {
            queueMoveable.add(new PathCard(random.nextInt(4), PathType.ANGLE, TreasureType.NONE));
        }
        Collections.shuffle((List<?>) queueMoveable);

        List<List<PathCard>> pathcards = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            ArrayList<PathCard> pathCardsbutactual = new ArrayList<>();
            for (int j = 0; j < 7; j++){

                if(i % 2 == 0 && j % 2 == 0){
                    pathCardsbutactual.add(queue.poll());
                } else {
                    pathCardsbutactual.add(queueMoveable.poll());
                }
            }
            pathcards.add(pathCardsbutactual);
        }

        extraCard = queueMoveable.poll();

        return new Field(pathcards, extraCard);
    }

    /**
     * Creates all 24 treasure cards and shuffles their order
     * @return A list of all treasure cards for a game
     */
    private ArrayList<TreasureCard> createTreasureCards() {
        ArrayList<TreasureCard> treasureCards = new ArrayList<>();

        for(int i = 1; i < 25; i++) {
            String str = TreasureCardType.class.getDeclaredFields()[i].getName();
            String path = "/TC_" + str.charAt(0) + str.substring(1).toLowerCase() + ".png";
            treasureCards.add(new TreasureCard(path));
        }
        Collections.shuffle(treasureCards);

        return treasureCards;
    }


    /**
     * Create two CardStack objects, each destined for one
     * of two players. Each stack is evently split between
     * and contains 12 cards.
     * @return A list of CardStacks
     */
    public ArrayList<CardStack> createCardStacks() {
        ArrayList<CardStack> cardStacks = new ArrayList<>();
        ArrayList<TreasureCard> treasureCards = createTreasureCards();

        ArrayList<TreasureCard> firstHalf = new ArrayList<>(treasureCards.subList(0,12));
        ArrayList<TreasureCard> secondHalf = new ArrayList<>(treasureCards.subList(12,24));
        CardStack cardStack1 = new CardStack(firstHalf);
        CardStack cardStack2 = new CardStack(secondHalf);

        cardStack1.setTotalCards(firstHalf);
        cardStack2.setTotalCards(secondHalf);

        cardStack1.setCurrentCard(cardStack1.getTotalCards().get(0));
        cardStack2.setCurrentCard(cardStack2.getTotalCards().get(0));

        cardStacks.add(cardStack1);
        cardStacks.add(cardStack2);
        return cardStacks;
    }
}
