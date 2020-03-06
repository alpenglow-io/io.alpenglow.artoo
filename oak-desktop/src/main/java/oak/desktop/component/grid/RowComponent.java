package oak.desktop.component.grid;

import oak.func.$2.Cons;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface RowComponent extends Cons<GridPane, Integer> {
  static RowComponent row(RowProperty... properties) {
    return new RowComponentImpl(new RowConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  static RowComponent autoRow(RowProperty... properties) {
    return new RowComponentImpl(new RowConstraints(), properties);
  }

  RowComponent with(ColumnComponent... components);
}
