package oak.desktop.event;

import io.martian.internal.fx.dsl.property.LabeledProperty;
import io.martian.internal.lang.Consumer2;
import io.martian.internal.lang.Function1;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;

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
