package re.artoo.fxcalibur;

import javafx.scene.Node;

@FunctionalInterface
public interface Element<ELEMENT> {
  ELEMENT render();
}
