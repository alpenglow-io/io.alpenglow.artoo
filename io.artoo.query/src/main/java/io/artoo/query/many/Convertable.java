package io.artoo.query.many;

import io.artoo.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.value.UInt32.*;

public interface Convertable<T extends Record> extends Queryable<T> {
  default @NotNull <K, E> Map<? extends K, ? extends E> asMap(final Function<? super T, ? extends K> key, final Function<? super T, ? extends E> element) {
    nonNullable(key, "key");
    nonNullable(element, "element");
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : this)
      map.put(key.apply(value), element.apply(value));
    return map;
  }

  default Collection<T> asCollection() {
    return asList();
  }

  default List<T> asList() {
    final List<T> list = new ArrayList<>();
    for (final var value : this)
      list.add(value);
    return list;
  }

  default T[] asArray(final Function<? super Integer, T[]> initializer) {
    for (final var count : ((Countable<T>) this::iterator).count().or(ZERO)) {
      return nonNullable(initializer, "initializer").apply(count.raw());
    }

    throw new IllegalStateException("Can't initialize array");
  }
}
