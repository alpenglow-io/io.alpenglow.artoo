package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

@FunctionalInterface
public interface Triggerable<T> extends Queryable<T> {
  default Case<T> fire(final TryConsumer1<? super T> operation) {
    return new Case<T>() {
      private final Queryable<T> origin = Triggerable.this;

      @Override
      public One<T> when(TryIntPredicate1<? super T> condition) {
        return () -> origin.cursor().filter(condition).peek(operation);
      }

      @Override
      public Cursor<T> cursor() {
        return origin.cursor().peek(operation);
      }
    };
  }

  default Case<T> raise(String message, TryFunction1<? super String, ? extends Throwable> operation) {
    return new Case<>() {
      private final Queryable<T> origin = Triggerable.this;

      @Override
      public One<T> when(TryIntPredicate1<? super T> condition) {
        return () -> origin.cursor().filter(condition.oppose()).or(message, operation);
      }

      @Override
      public Cursor<T> cursor() {
        return origin.cursor().or(message, operation);
      }
    };
  }

  default Case<T> raise(TrySupplier1<? extends Throwable> operation) {
    return raise(null, ignored -> operation.invoke());
  }

}
