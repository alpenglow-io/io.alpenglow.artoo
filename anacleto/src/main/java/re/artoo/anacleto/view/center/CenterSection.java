package re.artoo.anacleto.view.center;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import re.artoo.fxcalibur.Component;

public final class CenterSection implements Component {
  @Override
  public Node get() {
    return pane.border(this::set);
  }

  private void set(BorderPane pane) {
    pane.setStyle("""
      -fx-background-color: #F3F6F9;
      """);
  }
}
