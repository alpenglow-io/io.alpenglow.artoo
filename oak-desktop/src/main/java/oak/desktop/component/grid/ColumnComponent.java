package oak.desktop.component.grid;

import io.martian.internal.fx.dsl.component.Component;
import io.martian.internal.lang.Consumer3;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.function.Supplier;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface ColumnComponent extends Supplier<ColumnConstraints>, Consumer3<GridPane, Integer, Integer> {
  static ColumnComponent column(ColumnProperty... properties) {
    return new ColumnComponentImpl(new ColumnConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  ColumnComponent with(Component component);
}
