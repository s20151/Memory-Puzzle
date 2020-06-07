package sample;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


import java.net.URISyntaxException;


public class Card extends StackPane {
    private ImageView iv;
    private String path;
    private boolean isClickable;
    private Rectangle rectangle;
    public Card (String p) throws URISyntaxException {
        this.path=p;
        rectangle = new Rectangle (80,80);
        rectangle.setFill (Color.DARKGRAY);
        rectangle.setStroke (Color.BLACK);
        Image image = new Image(getClass().getResource("img/"+this.path).toURI().toString(),79,79,false,false);
        iv = new ImageView();
        iv.setImage(image);
        setAlignment (Pos.CENTER);
        getChildren ().addAll(rectangle,iv);
        hiddenAtStart ();
        this.isClickable=true;
    }
    public String getPath () {
        return path;
    }
    public void shown(){
        iv.setOpacity (1);
        this.isClickable=false;
    }
    public void hiddenAtStart(){
        iv.setOpacity (0);
    }

    public boolean isClickable () {
        return isClickable;
    }

    public void cardFound(){
        rectangle.setFill (null);
        rectangle.setStroke (null);
        FadeTransition ft = new FadeTransition (Duration.millis(700), iv);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished (e->isClickable=false);
    }
    public void hidden(){
        FadeTransition ft = new FadeTransition (Duration.millis(700), iv);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished (e->isClickable=true);
    }
}
