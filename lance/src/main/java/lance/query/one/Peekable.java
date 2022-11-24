package lance.query.one;

import lance.func.Cons;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final Cons.TryConsumer<? super T> peek) {
    return () -> cursor().map(new Peek<T, T>((i, it) -> peek.tryAccept(it)));
  }

  default One<T> exceptionally(Cons.TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
