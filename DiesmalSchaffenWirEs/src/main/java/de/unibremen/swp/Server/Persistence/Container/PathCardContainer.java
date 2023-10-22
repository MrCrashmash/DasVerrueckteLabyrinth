package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Cards.PathCard;

import javax.swing.*;

/**
 * PathCardContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class PathCardContainer {
    public int idCnt = 0;

    public int rotation;
    public String pathType;
    public String treasureType;
    public boolean isCorner;
    public int id = 0;

    /**
     * Construct Container
     * @param pathCard The PathCard
     */
    public PathCardContainer(PathCard pathCard){
        idCnt = PathCard.idCnt;
        rotation = pathCard.getRotation();
        pathType = pathCard.getPathType();
        treasureType = pathCard.getTreasureType();
        isCorner = pathCard.isCorner();
        id = pathCard.getId();
    }

    /**
     * Returns the Pathcard
     * @return Pathcard made from this PathCardContainer
     */
    public PathCard getPathcard(){
        PathCard pathCard =  new PathCard(rotation,pathType,treasureType);
        pathCard.setId(id);
        return  pathCard;
    }

    /**
     * Override toString for output
     * @return PathCard id as string
     */
    @Override
    public String toString(){
        return ""+id;
    }
}
