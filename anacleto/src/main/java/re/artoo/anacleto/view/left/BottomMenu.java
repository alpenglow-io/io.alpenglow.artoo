package re.artoo.anacleto.view.left;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.Component;
import re.artoo.lance.func.TryConsumer1;

import static re.artoo.anacleto.view.left.BottomMenu.Customization.style;

public final class BottomMenu implements Component {
  private final Component exitButton;

  public BottomMenu(Component exitButton) {
    this.exitButton = exitButton;
  }

  @Override
  public Node get() {
    return pane.vertical(style, exitButton);
  }

  interface Customization {
    TryConsumer1<VBox> style = box -> box.setStyle("""
      -fx-spacing: 16;
      -fx-background-color: red;
      """);
  }
}
