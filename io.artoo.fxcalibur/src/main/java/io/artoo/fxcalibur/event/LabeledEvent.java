package io.artoo.fxcalibur.event;

import io.artoo.fxcalibur.property.LabeledProperty;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;

@FunctionalInterface
public interface LabeledEvent {
  static LabeledEvent mouseClicked(Function<MouseEvent, LabeledProperty> callback) {
    return it -> it.setOnMouseClicked(e -> callback.apply(e).onLabeled(it));
  }

  static LabeledEvent mouseClicked(BiConsumer<MouseEvent, Labeled> callback) {
    return it -> it.setOnMouseClicked(e -> callback.accept(e, it));
  }

  void onLabeled(Labeled labeled);
}
