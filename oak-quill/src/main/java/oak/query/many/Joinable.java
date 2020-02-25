package oak.query.many;

import oak.func.$2.Pre;
import oak.query.Queryable;
import oak.query.many.Joinable.Joining;
import oak.query.many.$2.Many;
import oak.query.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.IntFunction;

public interface Joinable<O> extends Queryable<O> {
  default <I> Joining<O, I> join(final Queryable<I> second) {
    return new Join<>(this, second);
  }

  @SuppressWarnings("unchecked")
  default <I> Joining<O, I> join(final I... values) {
    return join(oak.query.Many.from(values));
  }

  interface Joining<O, I> {
    Many<O, I> on(final Pre<? super O, ? super I> expression);
  }
}

final class Join<O, I> implements Joining<O, I> {
  private final Queryable<O> first;
  private final Queryable<I> second;

  @Contract(pure = true)
  Join(final Queryable<O> first, final Queryable<I> second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public final Many<O, I> on(final Pre<? super O, ? super I> expression) {
    final var array = new ArrayList<Tuple2<O, I>>();
    for (final var o : first) {
      for (final var i : second) {
        if (expression.test(o, i))
          array.add(Tuple.of(o, i));
      }
    }
    return Many.of(array.toArray((IntFunction<Tuple2<O, I>[]>) Tuple2[]::new));
  }
}
