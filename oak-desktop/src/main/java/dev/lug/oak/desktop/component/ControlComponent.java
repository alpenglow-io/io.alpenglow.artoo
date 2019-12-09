package dev.lug.oak.desktop.component;

import dev.lug.oak.desktop.event.ControlEvent;
import dev.lug.oak.desktop.property.ControlProperty;
import dev.lug.oak.desktop.property.IdProperty;
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
