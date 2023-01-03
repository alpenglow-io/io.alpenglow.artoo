package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Peek;

public interface Peekable<T> extends Queryable<T> {
  default Many<T> peek(TryConsumer1<? super T> peek) {
    return peek((index, it) -> peek.invoke(it));
  }

  default Many<T> peek(TryConsumer2<? super Integer, ? super T> peek) {
    return () -> cursor().map(new Peek<>(peek));
  }

  default Many<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

