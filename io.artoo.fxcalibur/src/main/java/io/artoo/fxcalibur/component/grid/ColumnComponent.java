package io.artoo.fxcalibur.component.grid;  private <N extends Number, V> One<V> extreme(Extreme.Type type, final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(new Extreme<Pair<A, B>, N, V>(null, type, pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }



import io.artoo.fxcalibur.component.Component;
import io.artoo.lance.test.func.Cons;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.function.Supplier;

import static javafx.scene.layout.ConstraintsBase.CONSTRAIN_TO_PREF;

public interface ColumnComponent extends Supplier<ColumnConstraints>, Cons.MaybeTriConsumer<GridPane, Integer, Integer> {
  static ColumnComponent column(ColumnProperty... properties) {
    return new ColumnComponentImpl(new ColumnConstraints(CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF, CONSTRAIN_TO_PREF), properties);
  }

  ColumnComponent with(Component component);
}
