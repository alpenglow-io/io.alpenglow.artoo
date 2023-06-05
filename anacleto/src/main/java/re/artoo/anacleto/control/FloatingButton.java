package re.artoo.anacleto.control;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import re.artoo.fxcalibur.Component;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.TRANSPARENT;
import static javafx.util.Duration.ZERO;

public final class FloatingButton extends Button implements Component {
  private static final Duration ANIMATION_DURATION = Duration.millis(200);

  public enum Toggle {Toggled, NotToggled}

  private final String text;
  private final Toggle toggle;

  public FloatingButton(String text, Toggle toggle) {
    this.text = text;
    this.toggle = toggle;
  }

  @Override
  public Node render() {
    setText(text);
    // Create a button and set its style
    setStyle("""
        -fx-border-width: 0;
        -fx-max-width: 48;
        -fx-min-width: 48;
        -fx-max-height: 48;
        -fx-min-height: 48;
        -fx-background-color: #FFFFFF;
        -fx-padding: 5;
        -fx-border-radius: 12;
        -fx-background-radius: 12;
        -fx-text-fill: #55C596;
        -fx-font-size: 24;
        -fx-font-weight: bold;
      """);
    setToggle();

    // Create a drop shadow effect
    var shadow = new DropShadow();
    shadow.setColor(TRANSPARENT);
    shadow.setOffsetX(2);
    shadow.setOffsetY(2);
    shadow.setRadius(5);

    // Apply the drop shadow effect to the button
    setEffect(shadow);

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
    setOnMouseEntered(event -> shadowAnimation.play());
    setOnMouseExited(event -> backShadowAnimation.play());
    return this;
  }

  private void setToggle() {
    switch (toggle) {
      case Toggled:
        setBackground(Background.fill(Color.web("FFFFFF")));
        setTextFill(Color.web("55C596"));
      case NotToggled:
        setBackground(Background.fill(Color.web("FFFFFF")));
        setTextFill(Color.web("55C596"));
    }
  }
}
