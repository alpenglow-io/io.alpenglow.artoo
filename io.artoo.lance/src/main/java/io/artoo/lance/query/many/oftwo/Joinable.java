package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.join.Join;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.tuple.Tuple;

public interface Joinable<T> extends Queryable<T> {
  default <R, Q extends Queryable<R>> Joining<T, R, Tuple.OfTwo<T, R>> join(Q queryable) {
    return new Default<>(this, queryable);
  }

  default <R> Joining<T, R, Tuple.OfTwo<T, R>> join(R... items) {
    return join(Many.from(items));
  }

  interface Joining<T, R, J extends Tuple.OfTwo<T, R>> extends Many<J> {
    Many<Tuple.OfTwo<T, R>> on(Pred.Bi<? super T, ? super R> on);

    default <N> Many<N> select$(Func.Bi<? super T, ? super R, ? extends N> func) {
      return select(it -> func.tryApply(it.first(), it.second()));
    }

    default <N, Q extends Queryable<N>> Many<N> selection$(Func.Bi<? super T, ? super R, ? extends Q> func) {
      return selection(it -> func.tryApply(it.first(), it.second()));
    }
  }

  final class Default<T, R, Q extends Queryable<R>> implements Joining<T, R, Tuple.OfTwo<T, R>> {
    private final Queryable<T> first;

    private final Q second;

    Default(final Queryable<T> first, final Q second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public Cursor<Tuple.OfTwo<T, R>> cursor() {
      return first.cursor().to(Join.natural(second.cursor()));
    }
    @Override
    public Many<Tuple.OfTwo<T, R>> on(final Pred.Bi<? super T, ? super R> on) {
      return () -> first.cursor().to(Join.inner(second.cursor(), on));
    }

  }
}
