package re.artoo.fxcalibur.ui.layout;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface Box {
  default Node vertical(Node... nodes) {
    return null;
  }

  default Node horizontal(Node... nodes) {
    return null;
  }

  final class Vertical extends VBox implements Box {}
  final class Horizontal extends HBox implements Box {}
}
