package re.artoo.anacleto.view;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.Component;
import re.artoo.lance.func.TryConsumer1;

import static re.artoo.anacleto.view.LeftPanel.Customization.style;

public final class LeftPanel extends VBox implements Component {
  private final Component topMenu;
  private final Component bottomMenu;

  public LeftPanel(Component topMenu, Component bottomMenu) {
    this.topMenu = topMenu;
    this.bottomMenu = bottomMenu;
  }

  @Override
  public Node render() {
    return pane.vertical(style, topMenu, bottomMenu);
  }

  interface Customization {
    TryConsumer1<VBox> style = box -> box.setStyle("""
      -fx-alignment: center;
      -fx-min-width: 84;
      -fx-min-height: 100%;
      -fx-spacing: 16;
      -fx-padding: 16 16 16 16;
      """);
  }
}
