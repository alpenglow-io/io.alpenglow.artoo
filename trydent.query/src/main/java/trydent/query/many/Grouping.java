package trydent.query.many;

import trydent.func.$2.Pred;
import trydent.query.Many;
import trydent.query.many.internal.Having;

import static trydent.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends trydent.query.$2.Many<K, trydent.query.Many<T>> {
  default trydent.query.$2.Many<K, Many<T>> having(final Pred<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }
}
