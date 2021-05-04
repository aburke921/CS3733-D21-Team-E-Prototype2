package edu.wpi.cs3733.D21.teamE.views;

import javafx.scene.text.Text;

/**
 * Textual Direction Step Class
 * To be displayed in the directions dialog tableview
 */
public class TextualDirectionStep {
    private Text icon;
    private String direction;

    /**
     * Constructor with both the Icon and Direction
     * @param icon The icon as a Text object
     * @param direction Direction String
     */
    public TextualDirectionStep(Text icon, String direction) {
        this.icon = icon;
        this.direction = direction;
    }

    /**
     * Icon getter
     * @return The icon as a Text object
     */
    public Text getIcon() {
        return icon;
    }

    /**
     * Icon setter
     * @param icon The icon as a Text object
     */
    public void setIcon(Text icon) {
        this.icon = icon;
    }

    /**
     * Direction getter
     * @return The direction string
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Direction setter
     * @param direction The direction as a string
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
