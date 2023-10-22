package de.unibremen.swp.Game.Components.Cards;

import de.unibremen.swp.Game.Components.Cards.Type.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * PathCard
 *
 * Object used to tile the playing field
 * Contains graphics and is manipulated by the player
 * during a game
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class PathCard extends JPanel {
    public static int idCnt = 0;
    private int rotation;
    private String pathType;
    private String treasureType;
    private boolean isCorner;
    private int id = 0;
    private JLabel sprite;

    /**
     * Constructs PathCard
     * @param rotation The rotation (between 0 and 3)
     * @param pathType The PathType
     * @param treasureType The TreasureType
     */
    public PathCard(final int rotation, final String pathType, final String treasureType) {
        id = idCnt++;
        this.rotation = rotation & 3;
        this.pathType = pathType;
        this.treasureType = treasureType;

        try {
            setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            if(String.valueOf(treasureType).equals(TreasureType.NONE)){
                sprite = new JLabel(new ImageIcon(getClass().getResource(pathType)));
            } else {
                sprite = new JLabel(new ImageIcon(getClass().getResource(treasureType)));
            }

            // Layout innerhalb der PathCard um Spieler etc. einfach darzustellen
            sprite.setLayout(new GridBagLayout());
            add(sprite);
            rotateComponent(this);
        } catch(Exception e) {
            this.invalidate();
            e.printStackTrace();
            System.out.println("Fehler beim einlesen");
        }
    }

    /**
     * Retrieve asset rotation angle
     * @return The rotation angle
     */
    private double getRotationTheta() {
        return (this.getRotation() * 90.0f);
    }

    /**
     * Rotate PathCard according to said rotation angle
     * @param card The PathCard to be rotated
     * @throws IOException Icon not found
     */
    public void rotateComponent(final PathCard card) throws IOException {
        ImageIcon icon = (ImageIcon)card.getSprite().getIcon();
        BufferedImage img;
        if(String.valueOf(treasureType).equals(TreasureType.NONE)){
            img = ImageIO.read(getClass().getResource(String.valueOf(pathType)));
        } else {
            img = ImageIO.read(getClass().getResource(String.valueOf(treasureType)));
        }


        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(getRotationTheta()), img.getWidth() / 2, img.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        img = op.filter(img, null);

        card.getSprite().setIcon(new ImageIcon(img));
    }


    /**
     * Retrieve sprite Label
     * @return Sprite Label
     */
    public JLabel getSprite() { return sprite; }

    /**
     * Retrieve rotation
     * @return Rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Set rotation
     * @param rotation Rotation
     */
    public void setRotation(int rotation) {
        this.rotation = rotation & 3;
    }

    /**
     * Retrieve path type
     * @return Path type
     */
    public String getPathType() {
        return pathType;
    }

    /**
     * Check corner status
     * @return Whether pathcard is corner card
     */
    public boolean isCorner() {
        return isCorner;
    }

    /**
     * Retrieve treasure type
     * @return Treasure type
     */
    public String getTreasureType(){
        return treasureType;
    }

    /**
     * Retrieve PathCard id
     * @return Id
     */
    public int getId() { return this.id; }


    /**
     * Stringify PathCard
     * @return
     */
    @Override
    public String toString(){

        return String.valueOf(id);
    }

    /**
     * Set PathCard id
     * @param id Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Equality override
     * @param obj PathCard
     * @return Whether or not they're equal
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof PathCard){
           return this.rotation == ((PathCard) obj).getRotation()
                    && this.pathType.equals(((PathCard) obj).getPathType())
                    && this.treasureType.equals(((PathCard) obj).getTreasureType())
                    && this.isCorner == ((PathCard) obj).isCorner
                    && this.id == ((PathCard) obj).id;
        } else {
            return false;
        }

    }
}
