package edu.wpi.cs3733.D21.teamE.views;

import javafx.scene.text.Text;

public class TextualDirectionStep {
    private Text icon;
    private String direction;

    public TextualDirectionStep(Text icon, String direction) {
        this.icon = icon;
        this.direction = direction;
    }

    public Text getIcon() {
        return icon;
    }

    public void setIcon(Text icon) {
        this.icon = icon;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
