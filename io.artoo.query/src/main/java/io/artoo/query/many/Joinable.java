package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.Joinable.BiProjectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Joinable<T1 extends Record> extends Queryable<T1> {
  default <T2 extends Record, Q extends Queryable<T2>> Joining<T1, T2> join(final Q second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2 extends Record> Joining<T1, T2> join(final T2... values) {
    return join(Many.from(values));
  }

  interface Joining<T1 extends Record, T2 extends Record> {
    <R extends Record> BiProjectable<T1, T2, R> on(final BiPredicate<? super T1, ? super T2> on);
  }

  interface BiProjectable<T1 extends Record, T2 extends Record, R extends Record> {
    Many<R> select(BiFunction<? super T1, ? super T2, ? extends R> select);
  }
}


final class Join<O extends Record, I extends Record> implements Joinable.Joining<O, I> {
  private final Queryable<O> first;
  private final Queryable<I> second;

  @Contract(pure = true)
  public Join(final Queryable<O> first, final Queryable<I> second) {
    this.first = first;
    this.second = second;
  }

  @Contract(pure = true)
  @Override
  public final <R extends Record> @NotNull BiProjectable<O, I, R> on(BiPredicate<? super O, ? super I> on) {
    return select -> {
      final var array = new ArrayList<R>();
      for (final var o : first) {
        for (final var i : second) {
          if (on.test(o, i))
            array.add(select.apply(o, i));
        }
      }
      return Many.from(array);
    };
  }
}


