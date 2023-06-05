package re.artoo.anacleto.view.left;

import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.Component;
import re.artoo.lance.func.TryConsumer1;

import static re.artoo.anacleto.view.left.TopMenu.Customization.style;

public final class TopMenu implements Component {
  private final ToggleGroup group = new ToggleGroup();
  private final Component homeButton;
  private final Component usrsButton;
  private final Component chatButton;
  private final Component sttsButton;
  private final Component setsButton;

  public TopMenu(Component homeButton, Component usrsButton, Component chatButton, Component sttsButton, Component setsButton) {
    this.homeButton = homeButton;
    this.usrsButton = usrsButton;
    this.chatButton = chatButton;
    this.sttsButton = sttsButton;
    this.setsButton = setsButton;
  }

  @Override
  public Node render() {
    return pane.vertical(style,
      homeButton,
      usrsButton,
      chatButton,
      sttsButton,
      setsButton
    );
  }

  interface Customization {
    TryConsumer1<VBox> style = box -> box.setStyle("""
      -fx-alignment: center;
      -fx-spacing: 16;
      """);
  }
}
