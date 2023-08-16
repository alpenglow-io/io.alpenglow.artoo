package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.*;
import re.artoo.lance.query.One;
import re.artoo.lance.query.one.internal.Trapped;

public interface Exceptionable<T> extends Queryable<T> {
  sealed interface Trapping<T> permits Trapped {
    One<T> when(TryPredicate1<? super T> condition);
  }

  default One<T> trap(TryConsumer1<? super Throwable> operation) {
    return () -> cursor().exceptionally(operation);
  }

  default Trapping<T> raise(String message, TryFunction1<? super String, ? extends Throwable> operation) {
    return new Trapped<>(message, operation, cursor());
  }

  default Trapping<T> raise(TrySupplier1<? extends Throwable> operation) {
    return new Trapped<>(null, it -> operation.invoke(), cursor());
  }

  default One<T> recover(TryFunction1<? super Throwable, ? extends T> operation) {
    return () -> cursor().eventually(operation);
  }
}
