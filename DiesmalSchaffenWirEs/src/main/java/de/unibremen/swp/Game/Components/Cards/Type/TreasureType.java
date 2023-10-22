package de.unibremen.swp.Game.Components.Cards.Type;

/**
 * TreasureType
 *
 * Interface for treasure type constants
 *
 * @author Mut Daniel, Drescher Lennart
 */
public interface TreasureType {

    public static final String NONE = "";

    public static final String BONE = "/p_T_Bone.jpg";

    public static final String BOOK = "/p_T_Book.jpg";

    public static final String CANDLE = "/p_T_Candle.jpg";

    public static final String CRYSTAL = "/p_T_Crystal.jpg";

    public static final String EYE = "/p_T_Eye.jpg";

    public static final String GEAR = "/p_T_Gear.jpg";

    public static final String HEART = "/p_T_Heart.jpg";

    public static final String KEY = "/p_T_Key.jpg";

    public static final String MAP = "/p_T_Map.jpg";

    public static final String PAPER = "/p_T_Paper.jpg";

    public static final String POTIONB = "/p_T_Potionb.jpg";

    public static final String POTIONY = "/p_T_Potiony.jpg";

    public static final String SKULL = "/p_T_Skull.jpg";

    public static final String FEATHER = "/p_T_Feather.jpg";

    public static final String SHROOM = "/p_T_Shroom.jpg";

    public static final String ARROW = "/p_T_Arrow.jpg";

    public static final String DIAMOND = "/p_T_Diamond.jpg";

    public static final String WINE = "/p_T_Wine.jpg";

    public static final String SILVERBAR = "/p_L_Silverbar.jpg";

    public static final String BOTTLE = "/p_L_Bottle.jpg";

    public static final String BRONZEBAR = "/p_L_Bronzebar.jpg";

    public static final String GOLDBAR = "/p_L_Goldbar.jpg";

    public static final String SWORD = "/p_L_Sword.jpg";

    public static final String TORCH = "/p_L_Torch.jpg";

    /**
     * Used for field generation
     */
    public static final String[] angleTypes = new String[]{
            SILVERBAR,
            BOTTLE,
            BRONZEBAR,
            GOLDBAR,
            SWORD,
            TORCH
    };

    /**
     * Used for field generation
     */
    public static final String[] junctionTypes = new String[]{
            CRYSTAL,
            FEATHER,
            SHROOM,
            DIAMOND,
            ARROW,
            WINE
    };

}
