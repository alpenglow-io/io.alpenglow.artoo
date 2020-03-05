package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.$2.IntPred;
import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.internal.Where;

import static oak.func.$2.IntFunc.identity;
import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.apply(param));
  }

  default Many<T> where(final IntPred<? super T> where) {
    return where(where, identity());
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    nonNullable(type, "type");
    return where((index, it) -> type.isInstance(it), (index, it) -> type.cast(it));
  }

  default <R> Many<R> where(final IntPred<? super T> where, final IntFunc<? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, where, select)::iterator;
  }
}

