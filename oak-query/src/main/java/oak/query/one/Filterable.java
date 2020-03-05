package oak.query.one;

import oak.func.$2.IntFunc;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.internal.Where;
import oak.query.One;

import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(Pred<? super T> where) {
    nonNullable(where, "where");
    return new Where<>(this, (index, it) -> where.test(it), IntFunc.identity())::iterator;
  }

  default <R> One<R> ofType(Class<? extends R> type) {
    nonNullable(type, "type");
    return new Where<T, R>(this, ((index, it) -> type.isInstance(it)), (index, it) -> type.cast(it))::iterator;
  }
}
