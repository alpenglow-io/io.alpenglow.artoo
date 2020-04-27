package artoo.query.many;

import artoo.func.$2.FuncInt;
import artoo.func.$2.PredInt;
import artoo.func.Pred;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.impl.Where;

import static artoo.func.$2.FuncInt.identity;
import static artoo.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.apply(param));
  }

  default Many<T> where(final PredInt<? super T> where) {
    return where(where, identity());
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    nonNullable(type, "type");
    return where((index, it) -> type.isInstance(it), (index, it) -> type.cast(it));
  }

  default <R> Many<R> where(final PredInt<? super T> where, final FuncInt<? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, where, select)::iterator;
  }
}

