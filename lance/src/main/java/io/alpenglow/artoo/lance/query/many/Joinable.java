package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.query.cursor.routine.join.Join;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Joinable<T> extends Queryable<T> {
  default <R, Q extends Queryable<R>> Joining<T, R> join(Q queryable) {
    return new Default<>(this, queryable);
  }

  default <R> Joining<T, R> join(R... items) {
    return join(Many.from(items));
  }

  interface Joining<A, B> extends Many.OfTwo<A, B> {
    Many.OfTwo<A, B> on(TryPredicate2<? super A, ? super B> on);
  }

  final class Default<A, B, Q extends Queryable<B>> implements Joining<A, B> {
    private final Queryable<A> first;

    private final Q second;

    Default(final Queryable<A> first, final Q second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public Cursor<Pair<A, B>> cursor() {
      return first.cursor().to(Join.natural(second.cursor()));
    }
    @Override
    public Many.OfTwo<A, B> on(final TryPredicate2<? super A, ? super B> on) {
      return () -> first.cursor().to(Join.inner(second.cursor(), on));
    }

  }
}
