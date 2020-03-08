package oak.query.many;

import oak.func.$2.ConsInt;
import oak.func.$2.PredInt;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.One;
import oak.query.many.internal.Quantify;

import static oak.func.$2.PredInt.tautology;
import static oak.type.Nullability.nonNullable;

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

