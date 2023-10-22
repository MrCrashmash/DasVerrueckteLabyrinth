package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Cards.CardStack;
import de.unibremen.swp.Game.Components.Cards.TreasureCard;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CardsStackContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class CardsStackContainer {
    public TreasureCardContainer currentCard;
    public ArrayList<TreasureCardContainer> totalCards;
    public ArrayList<TreasureCardContainer> obtainedCards;

    /**
     * Construct Container
     * @param cardStack The cardstack
     */
    public CardsStackContainer(CardStack cardStack){
        currentCard = new TreasureCardContainer(cardStack.getCurrentCard());
        totalCards = cardStack
                .getTotalCards()
                .stream()
                .flatMap(treasureCard -> Stream.of(new TreasureCardContainer(treasureCard)))
                .collect(Collectors.toCollection(ArrayList::new));

        obtainedCards = cardStack
                .getObtainedCards()
                .stream()
                .flatMap(treasureCard -> Stream.of(new TreasureCardContainer(treasureCard)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the CardStack from the Container
     * @return CardStack made from List of TreasureCardContainers
     */
    public CardStack getCardStackFromContainer(){
        CardStack cardStack = new CardStack(totalCards
                .stream()
                .flatMap(treasureCard -> Stream.of(treasureCard.getTreasureCardFromContainer()))
                .collect(Collectors.toCollection(ArrayList::new)));

        cardStack.setCurrentCard(new TreasureCard(currentCard.getType()));
        cardStack.setObtainedCards(obtainedCards
                .stream()
                .flatMap(treasureCard -> Stream.of(treasureCard.getTreasureCardFromContainer()))
                .collect(Collectors.toCollection(ArrayList::new)));

        return cardStack;
    }
}
