package oak.quill.query;

import oak.collect.Many;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.query.Joinable.Joining;
import oak.quill.query.tuple.Queryable2;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Joinable<O> extends Structable<O> {
  default <I> Joining<O, I> join(final Structable<I> second) {
    return new Join<>(this, second);
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
    final var many = Many.<Tuple2<O, I>>of();
    for (final var o : first) {
      for (final var i : second) {
        if (expression.test(o, i))
          many.add(Tuple.of(o, i));
      }
    }
    return Queryable2.of(many);
  }
}
