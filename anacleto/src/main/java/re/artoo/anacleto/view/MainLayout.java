package re.artoo.anacleto.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import re.artoo.fxcalibur.Component;

public final class MainLayout implements Component {
  private final Component leftPanel;
  private final Component rightPanel;

  public MainLayout(Component leftPanel, Component rightPanel) {
    this.leftPanel = leftPanel;
    this.rightPanel = rightPanel;
  }

  @Override
  public Node render() {
/*
    setLeft(leftPanel.render());
    setRight(rightPanel.render());
*/
    final var borderPane = new BorderPane();
    borderPane.setStyle("""
      -fx-background-size: 1440 900;
      -fx-background-radius: 36 36 36 36;
      -fx-border-radius: 36 36 36 36;
      -fx-background-color: #55C596;
      """);
    borderPane.setCenter(new Button("Hello"));

    return borderPane;
  }
}
