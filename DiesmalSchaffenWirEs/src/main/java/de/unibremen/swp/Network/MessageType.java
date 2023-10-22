package de.unibremen.swp.Network;

/**
 * MessageType
 *
 * Interface for message id constants
 *
 * @author Mut Daniel, Drescher Lennart
 */
public interface MessageType {

    public static final String FIELD_UPDATE = "updateField";

    public static final String MOVE_VALIDATE = "requestMoveValidate";

    public static final String INSERT_VALIDATE = "requestInsertValidate";

    public static final String WINNER = "transmitWinner";

    public static final String SAVE_EXIT = "SaveAndExit";

    public static final String TURN = "transmitTurn";

    public static final String PAUSE = "pauseGame";

    public static final String RESUME = "resumeGame";

    public static final String DEBUG = "debug";

    public static final String CONFIRM_PAIRING = "pairingConfirmation";

    public static final String COLROW_UPDATE = "ColRowUpdate";

    public static final String PLAYER_UPDATE = "updatePlayer";

    public static final String CARDSTACK_UPDATE = "updateCardStack";


}
