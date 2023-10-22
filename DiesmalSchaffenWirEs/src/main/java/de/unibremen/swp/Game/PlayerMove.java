package de.unibremen.swp.Game;

import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Player;

import java.io.Serializable;

/**
 * PlayerMove
 *
 * Object that holds player move data
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class PlayerMove implements Serializable {
    private final int[] origin;
    private final int[] destination;

    /**
     * Construct
     * @param origin origin coords
     * @param destination destination coords
     */
    public PlayerMove(final int[] origin, final int[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Retrieve origin
     * @return Origin
     */
    public int[] getOrigin() {
        return origin;
    }

    /**
     * Retrieve destination
     * @return Destination
     */
    public int[] getDestination() {
        return destination;
    }
}
