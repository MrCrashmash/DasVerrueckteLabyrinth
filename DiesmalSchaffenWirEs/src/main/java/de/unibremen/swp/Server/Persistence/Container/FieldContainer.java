package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Field;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FieldContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class FieldContainer {

    public List<List<PathCardContainer>> pathCardPositions;
    public PathCardContainer extraCard;
    String tripletString;
    int tripletInt;
    int tripletInt2;

    public List<PlayerContainer> playerList;

    /**
     * Construct Container
     * @param field The field
     */
    public FieldContainer(Field field){
        extraCard = new PathCardContainer(field.getExtraCard());
        playerList = new ArrayList<>();
        playerList.add(new PlayerContainer(field.getPlayerList().get(0)));
        playerList.add(new PlayerContainer(field.getPlayerList().get(1)));

        tripletString = Field.getForbiddenPathMove().getValue0();
        tripletInt = Field.getForbiddenPathMove().getValue1();
        tripletInt2 = Field.getForbiddenPathMove().getValue2();

        pathCardPositions = new ArrayList<>();
        List<List<PathCard>> pathcardsOriginal = field.getPathCardPositions();
        for (int i = 0;i < pathcardsOriginal.size(); i++) {
            pathCardPositions.add(new ArrayList<>());
            for (int j = 0; j < pathcardsOriginal.get(i).size(); j++){
                pathCardPositions.get(i).add(new PathCardContainer(pathcardsOriginal.get(i).get(j)));
            }
        }

    }

    /**
     * Retrieve extra card
     * @return Extra card container
     */
    public PathCardContainer getExtraCard() {
        return extraCard;
    }

    /**
     * Returns the 2D List of Pathcards
     * @return List<List<PathCard>> made from the 2D List of PathCardContainers
     */
    public List<List<PathCard>> getPathCards(){
        PathCard.idCnt = 0;
        List<List<PathCard>> ausgabeListe = new ArrayList<>();

        for (int i = 0;i < pathCardPositions.size(); i++) {
            ausgabeListe.add(new ArrayList<>());
            for (int j = 0; j < pathCardPositions.get(i).size(); j++){
                ausgabeListe.get(i).add(pathCardPositions.get(i).get(j).getPathcard());
            }
        }
        return ausgabeListe;
    }

    /**
     * Returns the Field
     * @return Field made from the Container
     */
    public Field getFieldFromContainer(){
        Field field = new Field(getPathCards(),getExtraCard().getPathcard());
        Field.setForbiddenPathMove(new Triplet<>(tripletString, tripletInt, tripletInt2));
        field.setPlayerList(playerList
                .stream()
                .flatMap(playerContainer -> Stream.of(playerContainer.getPlayerFromContainer()))
                .collect(Collectors.toCollection(ArrayList::new)));

        return field;
    }

    /**
     * Retrieve forbidden move
     * @return Forbidden move
     */
    public Triplet<String,Integer,Integer> getForbiddenMove(){
        return new Triplet<>(tripletString,tripletInt,tripletInt2);
    }

    /**
     * Stringify PathCards
     * @return PathCards as string
     */
    @Override
    public String toString(){
        StringBuilder ausgabeString = new StringBuilder();
        for (List<PathCardContainer> l:pathCardPositions) {
            ausgabeString.append("\n");
            for (PathCardContainer p:l) {
                ausgabeString.append(" ").append(p);
            }
        }
        return  ausgabeString.toString();
    }
}
