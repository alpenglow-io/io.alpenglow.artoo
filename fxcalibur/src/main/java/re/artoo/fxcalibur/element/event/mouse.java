package re.artoo.fxcalibur.element.event;

import javafx.scene.input.MouseEvent;
import re.artoo.fxcalibur.element.Attribute;

import java.util.function.Consumer;

import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

public enum mouse {
  ;

  public static Attribute released(Consumer<MouseEvent> consumer) {
    return node -> node.addEventHandler(MOUSE_RELEASED, consumer::accept);
  }
}
