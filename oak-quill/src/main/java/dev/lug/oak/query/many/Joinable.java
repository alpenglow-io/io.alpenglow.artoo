package dev.lug.oak.query.many;

import dev.lug.oak.func.pre.Predicate2;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.many.Joinable.Joining;
import dev.lug.oak.query.many.tuple.Queryable2;
import dev.lug.oak.query.tuple.Tuple;
import dev.lug.oak.query.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.IntFunction;

public interface Joinable<O> extends Structable<O> {
  default <I> Joining<O, I> join(final Structable<I> second) {
    return new Join<>(this, second);
  }

  @SuppressWarnings("unchecked")
  default <I> Joining<O, I> join(final I... values) {
    return join(Queryable.from(values));
  }

  interface Joining<O, I> {
    Queryable2<O, I> on(final Predicate2<? super O, ? super I> expression);
  }
}

final class Join<O, I> implements Joining<O, I> {
  private final Structable<O> first;
  private final Structable<I> second;

  @Contract(pure = true)
  Join(final Structable<O> first, final Structable<I> second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public final Queryable2<O, I> on(final Predicate2<? super O, ? super I> expression) {
    final var array = new ArrayList<Tuple2<O, I>>();
    for (final var o : first) {
      for (final var i : second) {
        if (expression.test(o, i))
          array.add(Tuple.of(o, i));
      }
    }
    return Queryable2.of(array.toArray((IntFunction<Tuple2<O, I>[]>) Tuple2[]::new));
  }
}
