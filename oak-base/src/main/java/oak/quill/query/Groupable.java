package oak.quill.query;

import oak.collect.Many;
import oak.func.fun.Function1;
import oak.quill.Structable;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;
import static oak.func.fun.Function1.identity;

public interface Groupable<T> extends Structable<T> {
  default <K> Selectable<K, Collection<T>> groupBy(final Function1<? super T, ? extends K> key) {
    return groupBy(key, identity());
  }

  default <K, E> Selectable<K, Collection<E>> groupBy(final Function1<? super T, ? extends K> key, final Function1<? super T, ? extends E> element) {
    return new GroupBy<>(this, key, element);
  }
}

final class GroupBy<T, K, E> implements Selectable<K, Collection<E>> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Structable<T> structable;
  private final Function1<? super T, ? extends K> key;
  private final Function1<? super T, ? extends E> element;

  @Contract(pure = true)
  GroupBy(final Structable<T> structable, final Function1<? super T, ? extends K> key, final Function1<? super T, ? extends E> element) {
    this.structable = structable;
    this.key = key;
    this.element = element;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<K, Collection<E>>> iterator() {
    final var map = new TreeMap<K, Collection<E>>(comparator);
    for (final var value : structable) {
      final var k = key.apply(value);
      map.putIfAbsent(k, new ArrayList<>());
      map.get(k).add(element.apply(value));
    }
    final var array = Many.<Tuple2<K, Collection<E>>>of();
    for (final var entry : map.entrySet()) array.add(Tuple.of(entry.getKey(), entry.getValue()));
    return array.iterator();
  }
}
