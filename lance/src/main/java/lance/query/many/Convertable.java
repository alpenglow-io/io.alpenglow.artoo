package lance.query.many;

import lance.func.Func;
import lance.Queryable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lance.literator.cursor.routine.Routine.array;
import static lance.literator.cursor.routine.Routine.list;
import static lance.scope.Nullability.nonNullable;

public interface Convertable<T> extends Queryable<T> {
  default <K, E> Map<? extends K, ? extends E> asMap(final Func.TryFunction<? super T, ? extends K> key, final Func.TryFunction<? super T, ? extends E> element) {
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
