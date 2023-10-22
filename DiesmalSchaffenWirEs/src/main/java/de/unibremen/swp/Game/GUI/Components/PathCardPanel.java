package de.unibremen.swp.Game.GUI.Components;

import com.intellij.uiDesigner.core.GridLayoutManager;
import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Game.GUI.GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * PathCardPanel
 *
 * PathCard container panel
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class PathCardPanel extends JPanel {
    public int i;
    public int j;

    Field field;

    /**
     * Construct panel
     * @param field Field
     * @param x x Coordinate
     * @param y y Coordinate
     */
    public PathCardPanel(Field field, int x, int y){
        this.field = field;
        i = x;
        j = y;
        this.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.setMaximumSize(new Dimension(75, 75));
        this.setMinimumSize(new Dimension(75, 75));
        this.setPreferredSize(new Dimension(75, 75));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PathCard cur = (((PathCardPanel)e.getComponent()).getPathCard());
                GUI.field.setSelection(cur);
                //field.setSelection(cur);
                System.out.println((((PathCardPanel)e.getComponent()).getPathCard()));
                System.out.println("Current Pathcard: " + field.getPlayerList().get(0).getFigure().getCurrentPathCard()); //das mist
                System.out.println("Current PathType: " + cur);

            }
        });
    }

    /**
     * Retrieve PathCard
     * @return PathCard
     */
    public PathCard getPathCard(){
        return field.getPathCard(i,j);
    }

}
