package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.One;

@FunctionalInterface
public interface Triggerable<T> extends Queryable<T> {
  default One<T> fire(final TryConsumer1<? super T> operation) {
    return () -> cursor().peek(operation);
  }
}
