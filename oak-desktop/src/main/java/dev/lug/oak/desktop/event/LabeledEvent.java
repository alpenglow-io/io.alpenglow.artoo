package dev.lug.oak.desktop.event;

import dev.lug.oak.desktop.property.LabeledProperty;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function1;

@FunctionalInterface
public interface LabeledEvent {
  static LabeledEvent mouseClicked(Function1<MouseEvent, LabeledProperty> callback) {
    return it -> it.setOnMouseClicked(e -> callback.apply(e).onLabeled(it));
  }
  static LabeledEvent mouseClicked(Consumer2<MouseEvent, Labeled> callback) {
    return it -> it.setOnMouseClicked(e -> callback.accept(e, it));
  }

  void onLabeled(Labeled labeled);
}
