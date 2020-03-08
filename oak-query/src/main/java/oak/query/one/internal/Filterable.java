package oak.query.one.internal;

import oak.func.$2.FuncInt;
import oak.func.Pred;
import oak.query.internal.Where;
import oak.query.One;

import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Sneakable<T> {
  default One<T> where(Pred<? super T> where) {
    nonNullable(where, "where");
    return new Where<>(this, this.sneak(), (index, it) -> where.test(it), FuncInt.identity())::iterator;
  }

  default <R> One<R> ofType(Class<? extends R> type) {
    nonNullable(type, "type");
    return new Where<T, R>(this, this.sneak(), ((index, it) -> type.isInstance(it)), (index, it) -> type.cast(it))::iterator;
  }
}
