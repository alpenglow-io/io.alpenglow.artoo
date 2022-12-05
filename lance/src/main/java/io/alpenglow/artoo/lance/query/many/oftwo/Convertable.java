package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.alpenglow.artoo.lance.query.cursor.routine.Routine.list;
import static io.alpenglow.artoo.lance.scope.Nullability.nonNullable;

public interface Convertable<A, B> extends Queryable.OfTwo<A, B> {
  default <K, E> Map<? extends K, ? extends E> asMap(final TryFunction2<? super A, ? super B, ? extends K> key, final TryFunction2<? super A, ? super B, ? extends E> value) {
    nonNullable(key, "key");
    nonNullable(value, "element");
    final var map = new ConcurrentHashMap<K, E>();
    for (final var element : this)
      map.put(key.apply(element.first(), element.second()), value.apply(element.first(), element.second()));
    return map;
  }

  default List<Pair<A, B>> asList() {
    return cursor().as(list());
  }

  default Collection<Pair<A, B>> asCollection() {
    return asList();
  }

  default Iterable<Pair<A, B>> asIterable() {
    return asCollection();
  }

/*  default <P extends Pair<A, B>> P[] asArrayOf(Class<P> type) {
    return cursor().as(array(type));
  }*/
}
