package oak.query.many;

import oak.func.Func;
import oak.func.IntFunc;
import oak.func.LongFunc;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static oak.func.Func.identity;
import static oak.query.one.Projectable.as;
import static oak.type.Nullability.nonNullable;

public interface Convertable<T> extends Queryable<T> {
  default @NotNull <K, E> Map<? extends K, ? extends E> asMap(final Func<? super T, ? extends K> key, final Func<? super T, ? extends E> element) {
    nonNullable(key, "key");
    nonNullable(element, "element");
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : this) map.put(key.apply(value), element.apply(value));
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

  default T[] asArray(final IntFunc<T[]> initializer) {
    nonNullable(initializer, "initializer");
    return ((Countable<T>) this::iterator).count().select(as(initializer::applyInt)).asIs();
  }
}
