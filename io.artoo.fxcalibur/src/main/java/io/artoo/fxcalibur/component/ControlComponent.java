package io.artoo.fxcalibur.component;

import io.artoo.fxcalibur.event.ControlEvent;
import io.artoo.fxcalibur.property.ControlProperty;
import io.artoo.fxcalibur.property.IdProperty;
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
