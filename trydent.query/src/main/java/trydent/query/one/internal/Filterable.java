package trydent.query.one.internal;

import trydent.func.$2.FuncInt;
import trydent.func.Pred;
import trydent.query.internal.Where;
import trydent.query.One;

import static trydent.type.Nullability.nonNullable;

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
