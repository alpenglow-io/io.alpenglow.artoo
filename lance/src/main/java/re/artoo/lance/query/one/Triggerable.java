package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.One;

@FunctionalInterface
public interface Triggerable<T> extends Queryable<T> {
  default Case<T> fire(final TryConsumer1<? super T> operation) {
    return condition -> () -> Triggerable.this.cursor().filter(condition).peek(operation);
  }

  default Case<T> raise(String message, TryFunction1<? super String, ? extends Throwable> operation) {
    return condition -> () -> Triggerable.this.cursor().filter(condition.oppose()).or(message, operation);
  }

  default Case<T> raise(TrySupplier1<? extends Throwable> operation) {
    return raise(null, ignored -> operation.invoke());
  }
}
