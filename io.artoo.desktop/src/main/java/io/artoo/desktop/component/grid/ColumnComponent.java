package io.artoo.desktop.component.grid;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import io.artoo.desktop.component.Component;
import io.artoo.func.$3.Cons;

import java.util.function.Supplier;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface ColumnComponent extends Supplier<ColumnConstraints>, Cons<GridPane, Integer, Integer> {
  static ColumnComponent column(ColumnProperty... properties) {
    return new ColumnComponentImpl(new ColumnConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  ColumnComponent with(Component component);
}
