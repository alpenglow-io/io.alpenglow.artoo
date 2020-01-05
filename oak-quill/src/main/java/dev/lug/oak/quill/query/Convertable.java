package dev.lug.oak.quill.query;

import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.IntFunction1;
import dev.lug.oak.quill.Structable;
import dev.lug.oak.quill.single.One;
import dev.lug.oak.type.As;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dev.lug.oak.func.fun.Function1.identity;
import static dev.lug.oak.func.pre.Predicate1.tautology;
import static dev.lug.oak.type.Nullability.nonNullable;
import static dev.lug.oak.type.Numeric.asNumber;
import static java.util.Objects.isNull;

public interface Convertable<T> extends Structable<T> {
  default @NotNull <K, E> Map<? extends K, ? extends E> asMap(final Function1<? super T, ? extends K> key, final Function1<? super T, ? extends E> element) {
    return new AsMap<>(this, nonNullable(key, "key"), nonNullable(element, "element")).eval();
  }

  default List<T> asList() {
    return new AsList<>(this).eval();
  }

  default T[] asArray(final IntFunction1<T[]> initializer) {
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
    ).eval();
  }
}

final class AsMap<T, K, E> implements As<Map<K, E>> {
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
  public final Map<K, E> eval() {
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : structable) {
      map.put(key.apply(value), element.apply(value));
    }
    return map;
  }
}

final class AsList<T> implements As<List<T>> {
  private final Structable<T> structable;

  @Contract(pure = true)
  AsList(final Structable<T> structable) {this.structable = structable;}

  @NotNull
  @Override
  public final List<T> eval() {
    final List<T> list = new ArrayList<>();
    for (final var value : structable)
      list.add(value);
    return list;
  }
}

final class AsArray<T> implements As<T[]> {
  private final One<Integer> count;
  private final Structable<T> structable;
  private final IntFunction1<T[]> initializer;

  @Contract(pure = true)
  AsArray(final One<Integer> count, final Structable<T> structable, final IntFunction1<T[]> initializer) {
    this.count = count;
    this.structable = structable;
    this.initializer = initializer;
  }

  @Override
  public final T[] eval() {
    final var array = count.select(initializer).iterator().next();
    var index = 0;
    for (final var value : structable) array[index++] = value;
    return array;
  }
}
