package artoo.query.many;

import org.jetbrains.annotations.NotNull;
import artoo.func.Func;
import artoo.func.FuncInt;
import artoo.query.Queryable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoo.type.Nullability.nonNullable;

public interface Convertable<T> extends Queryable<T> {
  default @NotNull <K, E> Map<? extends K, ? extends E> asMap(final Func<? super T, ? extends K> key, final Func<? super T, ? extends E> element) {
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

  default T[] asArray(final FuncInt<T[]> initializer) {
    nonNullable(initializer, "initializer");
    final var ts = ((Countable<T>) this::iterator)
      .count()
      .select(initializer::applyInt)
      .asIs();

    var i = 0;
    for (final var value : this) ts[i++] = value;

    return ts;
  }
}
