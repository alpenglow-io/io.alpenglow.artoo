package oak.query.many;

import oak.func.$2.Pred;
import oak.query.Many;
import oak.query.many.internal.Having;

import static oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends oak.query.$2.Many<K, oak.query.Many<T>> {
  default oak.query.$2.Many<K, Many<T>> having(final Pred<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }
}
