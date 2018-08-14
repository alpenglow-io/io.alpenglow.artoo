package oak.desktop.component;

import io.martian.internal.fx.dsl.event.ControlEvent;
import io.martian.internal.fx.dsl.property.ControlProperty;
import io.martian.internal.fx.dsl.property.IdProperty;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

public interface ControlComponent extends Source<Control> {
  static ControlComponent progressLoop(IdProperty id, ControlProperty... properties) {
    return new ControlComponentImpl<>(
      new ParentComponentImpl<>(
        () -> new ProgressIndicator(INDETERMINATE_PROGRESS), id), properties);
  }

  ControlComponent on(ControlEvent... events);
}
