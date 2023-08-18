package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

public interface Exceptionable<T> extends Queryable<T> {

  default One<T> trap(TryConsumer1<? super Throwable> operation) {
    return () -> cursor().exceptionally(operation);
  }

  default One<T> recover(TryFunction1<? super Throwable, ? extends T> operation) {
    return () -> cursor().eventually(operation);
  }
}
