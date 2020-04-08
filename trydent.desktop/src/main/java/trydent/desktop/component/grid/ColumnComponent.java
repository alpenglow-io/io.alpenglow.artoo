package trydent.desktop.component.grid;

import trydent.desktop.component.Component;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import trydent.func.$3.Cons;

import java.util.function.Supplier;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface ColumnComponent extends Supplier<ColumnConstraints>, Cons<GridPane, Integer, Integer> {
  static ColumnComponent column(ColumnProperty... properties) {
    return new ColumnComponentImpl(new ColumnConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  ColumnComponent with(Component component);
}
