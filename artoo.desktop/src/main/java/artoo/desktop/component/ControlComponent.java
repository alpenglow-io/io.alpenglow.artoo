package artoo.desktop.component;

import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import artoo.desktop.event.ControlEvent;
import artoo.desktop.property.ControlProperty;
import artoo.desktop.property.IdProperty;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

public interface ControlComponent extends Source<Control> {
  static ControlComponent progressLoop(IdProperty id, ControlProperty... properties) {
    return new ControlComponentImpl<>(
      new ParentComponentImpl<>(
        () -> new ProgressIndicator(INDETERMINATE_PROGRESS), id), properties);
  }

  ControlComponent on(ControlEvent... events);
}
