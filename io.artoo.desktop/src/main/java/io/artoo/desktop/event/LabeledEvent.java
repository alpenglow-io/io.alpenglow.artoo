package io.artoo.desktop.event;

import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import io.artoo.desktop.property.LabeledProperty;
import io.artoo.func.$2.Cons;
import io.artoo.func.Func;

@FunctionalInterface
public interface LabeledEvent {
  static LabeledEvent mouseClicked(Func<MouseEvent, LabeledProperty> callback) {
    return it -> it.setOnMouseClicked(e -> callback.apply(e).onLabeled(it));
  }

  static LabeledEvent mouseClicked(Cons<MouseEvent, Labeled> callback) {
    return it -> it.setOnMouseClicked(e -> callback.accept(e, it));
  }

  void onLabeled(Labeled labeled);
}
