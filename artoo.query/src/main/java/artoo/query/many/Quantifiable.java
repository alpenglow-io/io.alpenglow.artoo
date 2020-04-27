package artoo.query.many;

import artoo.func.$2.ConsInt;
import artoo.func.$2.PredInt;
import artoo.func.Pred;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.many.impl.Quantify;

import static artoo.func.$2.PredInt.tautology;
import static artoo.type.Nullability.nonNullable;

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

