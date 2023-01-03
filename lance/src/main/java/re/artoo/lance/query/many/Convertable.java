package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static re.artoo.lance.query.cursor.routine.Routine.array;
import static re.artoo.lance.query.cursor.routine.Routine.list;
import static re.artoo.lance.scope.Nullability.nonNullable;

public interface Convertable<T> extends Queryable<T> {
  default <K, E> Map<? extends K, ? extends E> asMap(final TryFunction1<? super T, ? extends K> key, final TryFunction1<? super T, ? extends E> element) {
    nonNullable(key, "key");
    nonNullable(element, "element");
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : this)
      map.put(key.apply(value), element.apply(value));
    return map;
  }

  default List<T> asList() {
    return cursor().as(list());
  }

  default Collection<T> asCollection() {
    return asList();
  }

  default Iterable<T> asIterable() {
    return asCollection();
  }

  default T[] asArrayOf(Class<T> type) {
    return cursor().as(array(type));
  }
}
