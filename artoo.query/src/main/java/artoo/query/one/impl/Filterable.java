package artoo.query.one.impl;

import artoo.func.$2.FuncInt;
import artoo.func.Pred;
import artoo.query.One;
import artoo.query.impl.Where;

import static artoo.type.Nullability.nonNullable;

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
