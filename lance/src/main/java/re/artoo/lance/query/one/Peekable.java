package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.One;

@FunctionalInterface
public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final TryConsumer1<? super T> peek) {
    return () -> cursor().map((index, it) -> { peek.invoke(it); return it; });
  }

  default One<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
