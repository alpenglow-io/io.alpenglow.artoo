package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import java.util.Arrays;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.lang.System.arraycopy;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return concat(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return () -> cursor().mapArray(elements -> {
      final var items = queryable.cursor().elements();
      final var length = elements.length + items.length;
      final var copied = Arrays.copyOf(elements, length);
      arraycopy(items, 0, copied, elements.length, length);
      return copied;
    });
  }
}

