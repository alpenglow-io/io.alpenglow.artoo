package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

public interface Exceptionable<T> extends Queryable<T> {
  default One<T> exception(TryConsumer1<? super Throwable> catch_) {
    return () -> cursor().exceptionally(catch_);
  }

  default One<T> rethrow(TryFunction1<? super Throwable, ? extends Throwable> rethrow) {
    return () -> cursor().exceptionally(rethrow);
  }

  default One<T> recover(TryFunction1<? super Throwable, ? extends T> recover) {
    return () -> cursor().exceptionally(recover::invoke);
  }
}
