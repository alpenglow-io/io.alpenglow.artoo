package io.artoo.query.many;

import io.artoo.query.Queryable;
import io.artoo.query.many.impl.GroupBy;

import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Function<? super T, ? extends K> key) {
    return new GroupBy<>(this, (i, it) -> {}, nonNullable(key, "key"));
  }
}
