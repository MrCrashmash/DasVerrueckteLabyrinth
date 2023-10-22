package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Figure;


/**
 * FigurContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class FigurContainer {

    public String spritePath;
    public String color;
    public PathCardContainer currentPathCard;

    /**
     * Construct Container
     * @param figure
     */
    public FigurContainer(Figure figure){
        spritePath = figure.getSpritePath();
        color = figure.getColor();
        currentPathCard = new PathCardContainer(figure.getCurrentPathCard());
    }

    /**
     * Returns the Figure
     * @return Figure made from the Container
     */
    public Figure getFigureFromContainer() {
        Figure figure = new Figure(spritePath,color);
        figure.setCurrentPathCard(currentPathCard.getPathcard());
        return figure;
    }
}
