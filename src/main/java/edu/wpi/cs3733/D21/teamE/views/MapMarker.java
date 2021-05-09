package edu.wpi.cs3733.D21.teamE.views;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

/**
 * Map Marker Class to simplify the drawing of paths
 */
public class MapMarker {
    private double xCoord;
    private double yCoord;
    private String size;
    private MarkerType type;

    public MapMarker(double xCoord, double yCoord, String size, MarkerType type) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.size = size;
        this.type = type;
    }

    public MaterialDesignIconView makeMarker() {
        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.MAP_MARKER);
        icon.setSize(size);
        icon.setLayoutX(xCoord);
        icon.setLayoutY(yCoord);

        switch (type) {
            case START:
                icon.setId("submission-icon");
                break;
            case END:
                icon.setId("black-icon");
                break;
            case FIRST: // First and Last currently not differentiated
            case LAST:
                icon.setId("red-icon");
                break;
            default:
                icon.setStyle("-fx-fill: TRANSPARENT"); // default error
        }

        return icon;
    }
}
