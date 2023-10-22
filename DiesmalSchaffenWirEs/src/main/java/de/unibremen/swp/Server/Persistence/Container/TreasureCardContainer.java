package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Cards.TreasureCard;


/**
 * TreasureCardContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class TreasureCardContainer {

    public String treasureCardType;

    /**
     * Constructs container
     * @param treasureCard The treasure card
     */
    public TreasureCardContainer(TreasureCard treasureCard){
        treasureCardType = treasureCard.getTreasureCardType();
    }

    /**
     * Retrieve treasure card type
     * @return Treasure card type
     */
    public String getType(){
        return treasureCardType;
    }

    /**
     * Returns the TreasureCard
     * @return TreasureCard made from the Container
     */
    public TreasureCard getTreasureCardFromContainer(){
        return new TreasureCard(treasureCardType);
    }
}
