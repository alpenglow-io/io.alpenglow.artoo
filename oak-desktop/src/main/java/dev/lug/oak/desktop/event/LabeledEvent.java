package dev.lug.oak.desktop.event;

import dev.lug.oak.desktop.property.LabeledProperty;
import oak.func.$2.Con;
import oak.func.Func;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;

@FunctionalInterface
public interface LabeledEvent {
  static LabeledEvent mouseClicked(Func<MouseEvent, LabeledProperty> callback) {
    return it -> it.setOnMouseClicked(e -> callback.apply(e).onLabeled(it));
  }
  static LabeledEvent mouseClicked(Con<MouseEvent, Labeled> callback) {
    return it -> it.setOnMouseClicked(e -> callback.accept(e, it));
  }

  void onLabeled(Labeled labeled);
}
