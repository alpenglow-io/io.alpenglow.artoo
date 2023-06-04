package re.artoo.anacleto.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import re.artoo.fxcalibur.Component;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.TRANSPARENT;
import static javafx.util.Duration.ZERO;

public final class WhiteButton implements Component {
  private static final Duration ANIMATION_DURATION = Duration.millis(200);
  @Override
  public Node render() {
    // Create a button and set its style
    Button button = new Button("\uD83C\uDFE0");
    button.setStyle("""
        -fx-border-width: 0;
        -fx-pref-width: 48;
        -fx-pref-height: 48;
        -fx-background-color: #FFFFFF;
        -fx-padding: 5;
        -fx-border-radius: 10px;
        -fx-background-radius: 10px;
        -fx-text-fill: #55C596;
        -fx-font-size: 24px;
      """);

    // Create a drop shadow effect
    var shadow = new DropShadow();
    shadow.setColor(TRANSPARENT);
    shadow.setOffsetX(2);
    shadow.setOffsetY(2);

    // Apply the drop shadow effect to the button
    button.setEffect(shadow);

    // Create an animation for the shadow effect on mouse over
    var shadowAnimation = new Timeline(
      new KeyFrame(ZERO, new KeyValue(shadow.colorProperty(), GRAY)),
      new KeyFrame(ANIMATION_DURATION, new KeyValue(shadow.offsetXProperty(), 5), new KeyValue(shadow.offsetYProperty(), 5), new KeyValue(shadow.colorProperty(), GRAY))
    );

    var backShadowAnimation = new Timeline(
      new KeyFrame(ZERO, new KeyValue(shadow.offsetXProperty(), 5), new KeyValue(shadow.offsetYProperty(), 5)),
      new KeyFrame(ANIMATION_DURATION, new KeyValue(shadow.offsetXProperty(), 2), new KeyValue(shadow.offsetYProperty(), 2), new KeyValue(shadow.colorProperty(), TRANSPARENT))
    );

    // Add the animation to the button's mouse over event
    button.setOnMouseEntered(event -> shadowAnimation.play());
    button.setOnMouseExited(event -> backShadowAnimation.play());
    return button;
  }
}
