package re.artoo.fxcalibur.ui.component;

import javafx.scene.Node;

public interface Avatar {
  default Node primary() {
    return null;
  }
}
