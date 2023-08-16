package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;

public interface Exceptionable<T> extends Queryable<T> {

  default Many<T> trap(TryConsumer1<? super Throwable> operation) {
    return () -> cursor().exceptionally(operation);
  }

  default Many<T> recover(TryFunction1<? super Throwable, ? extends T> operation) {
    return () -> cursor().eventually(operation);
  }
}
