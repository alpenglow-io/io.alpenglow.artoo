package io.artoo.query.many;

import io.artoo.func.$2.ConsInt;
import io.artoo.func.$2.PredInt;
import io.artoo.func.Pred;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Quantify;

import static io.artoo.func.$2.PredInt.tautology;
import static io.artoo.type.Nullability.nonNullable;

// TODO: replace One<Boolean> with OneBoolean (internal primitive boolean and not boxed-boolean)
public interface Quantifiable<T> extends Queryable<T> {
  default <C> One<Boolean> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Boolean> all(final Pred<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Boolean> all(final PredInt<? super T> where) {
    return new Quantify<>(this, ConsInt.nothing(), false, nonNullable(where, "where"))::iterator;
  }

  default One<Boolean> any() { return this.any(tautology()); }

  default One<Boolean> any(final PredInt<? super T> where) {
    return new Quantify<>(this, ConsInt.nothing(), true, nonNullable(where, "where"))::iterator;
  }
}

