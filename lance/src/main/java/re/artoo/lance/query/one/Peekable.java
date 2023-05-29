package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.One;

@FunctionalInterface
public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final TryConsumer1<? super T> then) {
    return () -> cursor().peek(then);
  }
}
