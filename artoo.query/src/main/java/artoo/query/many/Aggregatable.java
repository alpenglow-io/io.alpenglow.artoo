package artoo.query.many;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$2.Func;
import artoo.func.Pred;
import artoo.query.One;
import artoo.query.many.impl.Aggregate;

import static artoo.func.Cons.nothing;
import static artoo.func.Func.identity;
import static artoo.func.Pred.tautology;
import static artoo.type.Nullability.nonNullable;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremumable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred<? super T> where, final artoo.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<T, A, R>(this, nothing(), seed, nonNullable(where, "where"), nonNullable(select, "select"), nonNullable(aggregate, "aggregate"))::iterator;
  }

  default <A, R> One<A> aggregate(final A seed, final artoo.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), select, aggregate);
  }

  default <A, R> One<A> aggregate(final artoo.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, tautology(), select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final Func<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), identity(), aggregate);
  }

  default One<T> aggregate(final Func<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, tautology(), identity(), aggregate);
  }
}
