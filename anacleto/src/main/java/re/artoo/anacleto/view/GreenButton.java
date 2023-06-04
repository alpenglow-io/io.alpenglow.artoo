package re.artoo.anacleto.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Duration;
import re.artoo.fxcalibur.Component;

public record GreenButton(String text) implements Component {
  private static final Duration ANIMATION_DURATION = Duration.millis(200);
  @Override
  public Node render() {
    // Create a button and set its style
    Button button = new Button(text);
    button.setStyle("""
        -fx-border-width: 0;
        -fx-pref-width: 48;
        -fx-pref-height: 48;
        -fx-background-color: #55C596;
        -fx-padding: 5;
        -fx-border-radius: 10px;
        -fx-background-radius: 10px;
        -fx-text-fill: #FFFFFF;
        -fx-font-size: 24px;
      """);
    return button;
  }
}
