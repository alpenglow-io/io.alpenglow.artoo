package io.artoo.desktop.event;

import io.artoo.desktop.property.LabeledProperty;


import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;

@FunctionalInterface
public interface LabeledEvent {
  static LabeledEvent mouseClicked(Function<MouseEvent, LabeledProperty> callback) {
    return it -> it.setOnMouseClicked(e -> callback.apply(e).onLabeled(it));
  }

  static LabeledEvent mouseClicked(Consumer<MouseEvent, Labeled> callback) {
    return it -> it.setOnMouseClicked(e -> callback.accept(e, it));
  }

  void onLabeled(Labeled labeled);
}
