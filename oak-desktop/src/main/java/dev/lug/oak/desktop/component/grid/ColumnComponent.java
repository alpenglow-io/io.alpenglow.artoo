package dev.lug.oak.desktop.component.grid;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import dev.lug.oak.desktop.component.Component;
import dev.lug.oak.func.con.Consumer3;

import java.util.function.Supplier;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface ColumnComponent extends Supplier<ColumnConstraints>, Consumer3<GridPane, Integer, Integer> {
  static ColumnComponent column(ColumnProperty... properties) {
    return new ColumnComponentImpl(new ColumnConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  ColumnComponent with(Component component);
}
