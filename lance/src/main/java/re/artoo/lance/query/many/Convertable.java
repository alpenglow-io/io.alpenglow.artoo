package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    return cursor()
      .foldLeft(new ArrayList<T>(), (array, it) -> { array.add(it); return array; })
      .map(List::copyOf)
      .submit();
  }
  default List<T> asLinkedList() {
    return cursor()
      .foldLeft(new LinkedList<T>(), (linked, it) -> { linked.add(it); return linked; })
      .submit();
  }

  default Collection<T> asCollection() {
    return asList();
  }

  default Iterable<T> asIterable() {
    return asCollection();
  }

  default T[] asArray(TryIntFunction<? extends T[]> provider) {
    return cursor()
      .foldLeft(new ArrayList<T>(), (array, it) -> { array.add(it); return array; })
      .map(array -> array.toArray(provider::apply))
      .submit();
  }
}
