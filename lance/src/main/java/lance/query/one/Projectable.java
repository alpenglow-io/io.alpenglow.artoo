package lance.query.one;

import lance.func.Func;
import lance.func.Suppl;
import lance.func.tail.Select;
import lance.query.Many;
import lance.query.One;
import lance.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.MaybeFunction<? super T, ? extends R> select) {
    return () -> cursor().map(rec(Select.with(select)));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final Func.MaybeFunction<? super T, ? extends Q> selection) {
    return () -> cursor().map(rec(Select.with(selection))).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final Suppl.MaybeSupplier<? extends R> select) {
    return () -> cursor().map(rec(Select.with((i, it) -> select.tryGet())));
  }
}
