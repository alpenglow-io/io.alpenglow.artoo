package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.join.Join;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Joinable<T> extends Queryable<T> {
  default <R, Q extends Queryable<R>> Joining<T, R, Joining.Joined<T, R>> join(Q queryable) {
    return new Default<>(this, queryable);
  }

  interface Joining<T, R, J extends Joining.Joined<T, R>> extends Many<J> {
    Many<Joined<T, R>> on(Pred.Bi<? super T, ? super R> on);
  }
  
  final class Default<T, R, Q extends Queryable<R>> implements Joining<T, R, Joining.Joined<T, R>> {
    private final Queryable<T> first;

    private final Q second;

    Default(final Queryable<T> first, final Q second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public Cursor<Joined<T, R>> cursor() {
      return first.cursor().to(Join.natural(second.cursor()));
    }
    @Override
    public Many<Joined<T, R>> on(final Pred.Bi<? super T, ? super R> on) {
      return () -> first.cursor().to(Join.inner(second.cursor(), on));
    }

  }

  record Joined<T, R>(T first, R second) {}
}
