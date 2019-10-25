package oak.quill.query;

import oak.func.fun.Function1;
import oak.func.fun.IntFunction1;
import oak.quill.Structable;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static oak.func.fun.Function1.identity;
import static oak.func.pre.Predicate1.tautology;
import static oak.type.Nullability.nonNullable;

public interface Convertable<T> extends Structable<T> {
  default <K, E> Single<Map<K, E>> asMap(final Function1<? super T, ? extends K> key, final Function1<? super T, ? extends E> element) {
    return new AsMap<>(this, nonNullable(key, "key"), nonNullable(element, "element"));
  }

  default Single<List<T>> asList() {
    return new AsList<>(this);
  }

  default Single<T[]> asArray(final IntFunction1<T[]> initializer) {
    return new AsArray<>(
      new Aggregate<>(
        this,
        tautology(),
        identity(),
        0,
        (acc, next) -> acc + 1
      ),
      this,
      nonNullable(initializer, "initializer")
    );
  }
}

final class AsMap<T, K, E> implements Single<Map<K, E>> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends K> key;
  private final Function1<? super T, ? extends E> element;

  @Contract(pure = true)
  AsMap(final Structable<T> structable, final Function1<? super T, ? extends K> key, final Function1<? super T, ? extends E> element) {
    this.structable = structable;
    this.key = key;
    this.element = element;
  }

  @Override
  @NotNull
  public final Map<K, E> get() {
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : structable) {
      map.put(key.apply(value), element.apply(value));
    }
    return map;
  }
}

final class AsList<T> implements Single<List<T>> {
  private final Structable<T> structable;

  @Contract(pure = true)
  AsList(final Structable<T> structable) {this.structable = structable;}

  @NotNull
  @Override
  public final List<T> get() {
    final List<T> list = new ArrayList<>();
    for (final var value : structable)
      list.add(value);
    return list;
  }
}

final class AsArray<T> implements Single<T[]> {
  private final Single<Integer> count;
  private final Structable<T> structable;
  private final IntFunction1<T[]> initializer;

  @Contract(pure = true)
  AsArray(final Single<Integer> count, final Structable<T> structable, final IntFunction1<T[]> initializer) {
    this.count = count;
    this.structable = structable;
    this.initializer = initializer;
  }

  @Override
  public final T[] get() {
    final var array = count.select(initializer).iterator().next();
    var index = 0;
    for (final var value : structable) array[index++] = value;
    return array;
  }
}
