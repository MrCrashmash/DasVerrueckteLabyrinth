package de.unibremen.swp;
import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Cards.Type.PathType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureType;
import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Server.ServerLogic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Unit test for simple App.
 */
public class ServerLogicTest {

    /**
     * Generates a pre-set field object without randomization
     * for testing purposes
     * The field looks as follows when rendered in the GUI
     * https://i.imgur.com/CF386Pe.png
     * @return Field for testing purposes
     */
    public Field notRandomField(){
        PathCard extraCard;
        Queue<PathCard> queue = new LinkedList<>();

        queue.add(new PathCard(0, PathType.ANGLE, TreasureType.NONE));
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
        queue.add(new PathCard(2, PathType.ANGLE, TreasureType.NONE));

        Queue<PathCard> queueMoveable = new LinkedList<>();

        for(int i = 0; i < 12; i++) {
            queueMoveable.add(new PathCard(3, PathType.STRAIGHT, TreasureType.NONE));
        }

        for(int i = 0; i < 6; i++) {
            queueMoveable.add(new PathCard(2, PathType.JUNCTION, TreasureType.junctionTypes[i]));
        }

        for(int i = 0; i < 6; i++ ) {
            queueMoveable.add(new PathCard(1, PathType.ANGLE, TreasureType.angleTypes[i]));
        }

        for(int i = 0; i < 10; i++) {
            queueMoveable.add(new PathCard(0, PathType.ANGLE, TreasureType.NONE));
        }

        //FÃ¼llt Feld auf
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

    private ServerLogic logic = new ServerLogic(notRandomField());

    /**
     * Tests whether the pad method appropriately pads binary
     * numbers to a length of 4 by prepending zeros
     */
    @Test
    public void paddingTest() {
        assert(logic.pad(0).equals("0000"));
        assert(logic.pad(1).equals("0001"));
        assert(logic.pad(2).equals("0010"));
        assert(logic.pad(3).equals("0011"));
        assert(logic.pad(4).equals("0100"));
        assert(logic.pad(5).equals("0101"));
        assert(logic.pad(6).equals("0110"));
        assert(logic.pad(7).equals("0111"));
        assert(logic.pad(8).equals("1000"));
        assert(logic.pad(9).equals("1001"));
        assert(logic.pad(10).equals("1010"));
        assert(logic.pad(11).equals("1011"));
        assert(logic.pad(12).equals("1100"));
        assert(logic.pad(13).equals("1101"));
        assert(logic.pad(14).equals("1110"));
        assert(logic.pad(15).equals("1111"));
   }

    /**
     * Tests whether the circularRight method appropriately
     * bitshifts integers in a round-robin fashion
     * Bits that would be "shifted out" / lost during regular bitshift
     * operations such as >> and << are prepended
     */
   @Test
    public void circularShiftTest() {
       assert(logic.circularRight(1, 0) == 1);
       assert(logic.circularRight(1, 1) == 8);
       assert(logic.circularRight(1, 2) == 4);
       assert(logic.circularRight(1, 3) == 2);
       assert(logic.circularRight(1, 4) == 1);
   }

    /**
     * Tests whether neighborhood flags for PathCards with
     * a type of ANGLE are generated properly in all rotational configurations
     */
   @Test
    public void generateFlagAngle() {
       PathCard[] temp = new PathCard[4];
       for(int i = 0; i < 4; i++) {
           temp[i] = new PathCard(i, PathType.ANGLE, "");
       }

       assert(logic.generateFlag(temp[0]) == 6);
       assert(logic.generateFlag(temp[1]) == 3);
       assert(logic.generateFlag(temp[2]) == 9);
       assert(logic.generateFlag(temp[3]) == 12);
   }

    /**
     * Tests whether neighborhood flags for PathCards with
     * a type of STRAIGHT are generated properly in all rotational configurations
     */
    @Test
    public void generateFlagStraight() {
        PathCard[] temp = new PathCard[4];
        for(int i = 0; i < 4; i++) {
            temp[i] = new PathCard(i, PathType.STRAIGHT, "");
        }

        assert(logic.generateFlag(temp[0]) == 5);
        assert(logic.generateFlag(temp[1]) == 10);
        assert(logic.generateFlag(temp[2]) == 5);
        assert(logic.generateFlag(temp[3]) == 10);
    }

    /**
     * Tests whether neighborhood flags for PathCards with
     * a type of STRAIGHT are generated properly in all rotational configurations
     */
    @Test
    public void generateFlagJunction() {
        PathCard[] temp = new PathCard[4];
        for(int i = 0; i < 4; i++) {
            temp[i] = new PathCard(i, PathType.JUNCTION, "");
        }

        assert(logic.generateFlag(temp[0]) == 7);
        assert(logic.generateFlag(temp[1]) == 11);
        assert(logic.generateFlag(temp[2]) == 13);
        assert(logic.generateFlag(temp[3]) == 14);
    }




    /**
     * Tests whether a PathCards directly adjacent and accessible
     * PathCards are properly extracted
     */
    @Test
    public void neighborhoodTest1() {
        PathCard card = logic.getField().getPathCard(0, 0);

        assert(card.getPathType().equals(PathType.ANGLE));
        assert(card.getTreasureType().equals(TreasureType.NONE));

        ArrayList<PathCard> adjacentWalkableCards = logic.connections(card);

        assert(adjacentWalkableCards.size() == 1);
        assert(adjacentWalkableCards.contains(logic.getField().getPathCard(0, 1)));
    }

    /**
     * Tests whether a PathCards directly adjacent and accessible
     * PathCards are properly extracted
     */
    @Test
    public void neighborhoodTest2() {
        PathCard card = logic.getField().getPathCard(1, 1);

        assert(card.getPathType().equals(PathType.STRAIGHT));
        assert(card.getTreasureType().equals(TreasureType.NONE));

        ArrayList<PathCard> adjacentWalkableCards = logic.connections(card);

        assert(adjacentWalkableCards.size() == 2);
        assert(adjacentWalkableCards.contains(logic.getField().getPathCard(1, 0)));
        assert(adjacentWalkableCards.contains(logic.getField().getPathCard(1, 2)));
    }

    /**
     * Tests whether a PathCards directly adjacent and accessible
     * PathCards are properly extracted
     */
    @Test
    public void neighborhoodTest3() {
        PathCard card = logic.getField().getPathCard(3, 3);

        assert(card.getPathType().equals(PathType.JUNCTION));
        assert(card.getTreasureType().equals(TreasureType.ARROW));

        ArrayList<PathCard> adjacentWalkableCards = logic.connections(card);

        assert(adjacentWalkableCards.size() == 1);
        assert(adjacentWalkableCards.contains(logic.getField().getPathCard(4, 3)));
    }

    /**
     * Tests a given PathCard's coordinates on the field can be determined
     */
    @Test
    public void getIndicesTest1() {
        PathCard card = logic.getField().getPathCard(1, 1);
        int[] indices = logic.getField().getIndices(card);

        assert(indices[0] == 1);
        assert (indices[1] == 1);
    }

    /**
     * Tests whether all PathCards reachable from a given PathCard are found
     */
    @Test
    public void possiblePathsTest() {
        PathCard card = logic.getField().getPathCard(2, 2);

        assert(card.getPathType().equals(PathType.JUNCTION));
        assert(card.getTreasureType().equals(TreasureType.GEAR));

        ArrayList<PathCard> paths = logic.possiblePaths(card);

        assert(paths.size() == 14);
        assert(paths.contains(card));
        assert(paths.contains(logic.getField().getPathCard(2, 0))); //yellow
        assert(paths.contains(logic.getField().getPathCard(3, 0))); //feather
        assert(paths.contains(logic.getField().getPathCard(4, 0))); //heart
        assert(paths.contains(logic.getField().getPathCard(5, 0))); //torch
        assert(paths.contains(logic.getField().getPathCard(2, 1))); //straight
        assert(paths.contains(logic.getField().getPathCard(2, 3))); //straight
        assert(paths.contains(logic.getField().getPathCard(2, 4))); //eye
        assert(paths.contains(logic.getField().getPathCard(3, 4))); //bottle
        assert(paths.contains(logic.getField().getPathCard(4, 4))); //bone
        assert(paths.contains(logic.getField().getPathCard(4, 3))); //bar
        assert(paths.contains(logic.getField().getPathCard(3, 3))); //arrow
        assert(paths.contains(logic.getField().getPathCard(3, 2))); //diamond
        assert(paths.contains(logic.getField().getPathCard(4, 2))); //blue
    }

    /**
     * Tests whether moving to your current card (standing still)
     * is a valid move (it is as per the game rules)
     */
    @Test
    public void validateMoveSameCardTest() {
        int[] origin = new int[]{1, 1};
        assert(logic.validateMove(origin, origin));
    }

    /**
     * Tests whether moving to a connected card
     * is a valid move
     */
    @Test
    public void validateMoveDifferentCardTest() {
        int[] origin = new int[]{2, 2};
        int[] destination = new int[]{5, 0};
        assert(logic.validateMove(origin, destination));
    }

    /**
     * Tests whether moving to a non-connected card
     * is a valid move
     */
    @Test
    public void validateMoveUnreachableTest() {
        int[] origin = new int[]{2, 2};
        int[] destination = new int[]{0, 5};
        assert(!logic.validateMove(origin, destination));
    }
}
