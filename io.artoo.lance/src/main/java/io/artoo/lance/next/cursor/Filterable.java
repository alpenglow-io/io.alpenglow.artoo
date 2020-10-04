package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

public interface Filterable<T> extends Projectable<T> {
  default Cursor<T> where(Pred.Uni<? super T> pred) {
    return select(it -> pred.tryTest(it) ? it : null);
  }

  default Cursor<T> where(Pred.Bi<? super Integer, ? super T> pred) {
    return select((index, it) -> pred.tryTest(index, it) ? it : null);
  }

  default <R> Cursor<R> ofType(final Class<? extends R> type) {
    return select(it -> type.isInstance(it) ? type.cast(it) : null);
  }

  default <R> Cursor<T> when(Class<R> type, Cons.Uni<? super R> cons) {
    return select(new WhenType<>(type, cons));
  }

  default <N extends Number> Cursor<T> when(Pred.Uni<N> pred, Cons.Uni<? super N> cons) {
    return select(new WhenNumber<>(pred, cons));
  }
}

final class WhenType<T, R> implements Func.Uni<T, T> {
  private final Class<R> type;
  private final Cons.Uni<? super R> cons;

  WhenType(final Class<R> type, final Cons.Uni<? super R> cons) {
    this.type = type;
    this.cons = cons;
  }

  @Override
  public T tryApply(final T it) throws Throwable {
    if (type.isInstance(it)) cons.tryAccept(type.cast(it));
    return it;
  }
}

final class WhenNumber<T, N extends Number> implements Func.Uni<T, T> {
  private final Pred.Uni<N> pred;
  private final Cons.Uni<? super N> cons;

  WhenNumber(final Pred.Uni<N> pred, final Cons.Uni<? super N> cons) {
    this.pred = pred;
    this.cons = cons;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T tryApply(final T it) throws Throwable {
    if (it instanceof Number number) cons.tryAccept((N) number);
    return it;
  }
}
