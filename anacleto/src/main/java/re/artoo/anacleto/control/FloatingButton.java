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

public final class FloatingButton implements Component {
  private static final Duration ANIMATION_DURATION = Duration.millis(200);

  public enum Toggle {
    Toggled("#FFFFFF", "#55C596"),
    NotToggled("#55C596", "#FFFFFF");

    private final String background;
    private final String text;

    Toggle(String background, String text) {
      this.background = background;
      this.text = text;
    }
  }

  private final String text;
  private final Toggle toggle;

  public FloatingButton(String text, Toggle toggle) {
    this.text = text;
    this.toggle = toggle;
  }

  @Override
  public Node get() {
    Button button = new Button();
    button.setText(text);
    // Create a button and set its style
    button.setStyle("""
        -fx-border-width: 0;
        -fx-max-width: 48;
        -fx-min-width: 48;
        -fx-max-height: 48;
        -fx-min-height: 48;
        -fx-padding: 5;
        -fx-border-radius: 12;
        -fx-background-radius: 12;
        -fx-font-size: 24;
        -fx-background-color: %s;
        -fx-text-fill: %s;
      """.formatted(toggle.background, toggle.text));

    // Create a drop shadow effect
    var shadow = new DropShadow();
    shadow.setColor(TRANSPARENT);
    shadow.setOffsetX(2);
    shadow.setOffsetY(2);
    shadow.setRadius(5);

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
