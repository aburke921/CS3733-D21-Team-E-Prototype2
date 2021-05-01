package edu.wpi.cs3733.D21.teamE.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ImageObserver extends Observer{

    private ImageView imageView;

    public ImageObserver(Subject subject, ImageView imageView){
        this.subject = subject;
        this.imageView = imageView;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("ImageObserver");
        String floorNum = subject.getState();
        if (floorNum.equals("")) {return;}

        Image image = new Image("edu/wpi/cs3733/D21/teamE/maps/" + floorNum + ".png");
        imageView.setImage(image);
    }
}
