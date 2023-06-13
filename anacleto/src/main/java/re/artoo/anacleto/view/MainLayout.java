package re.artoo.anacleto.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import re.artoo.fxcalibur.Component;

public final class MainLayout implements Component {
  private final Component centerSection;
  private final Component leftPanel;
  private final Component rightPanel;

  public MainLayout(Component centerSection, Component leftPanel, Component rightPanel) {
    this.centerSection = centerSection;
    this.leftPanel = leftPanel;
    this.rightPanel = rightPanel;
  }

  @Override
  public Node get() {
    return pane.border(this::customize,
      centerSection,
      null,
      rightPanel,
      null,
      leftPanel
    );
  }

  private void customize(BorderPane pane) {
    pane.setStyle("""
      -fx-background-color: transparent;
      """);
  }
}
