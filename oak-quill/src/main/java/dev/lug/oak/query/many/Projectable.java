package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.IntFunction2;
import dev.lug.oak.query.Q;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.many.tuple.Queryable2;
import dev.lug.oak.query.many.tuple.Queryable3;
import dev.lug.oak.query.tuple.Tuple2;
import dev.lug.oak.query.tuple.Tuple3;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Projectable<T> extends Structable<T> {
  default <R> Queryable<R> select(final Q.AsJust<? super T, ? extends R> map) {
    return new Select<>(this, Nullability.nonNullable(map, "map"));
  }

  default <R> Queryable<R> select(final IntFunction2<? super T, ? extends R> mapIndex) {
    return new SelectIth<>(this, Nullability.nonNullable(mapIndex, "mapIndex"));
  }

  default <T1, T2, U extends Tuple2<T1, T2>> Queryable2<T1, T2> select(final Q.JustAsTuple2<? super T, ? extends U> tuple) {
    return new SelectTuple2<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <T1, T2, T3, U extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select(final Q.JustAsTuple3<? super T, ? extends U> tuple) {
    return new SelectTuple3<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <R, S extends Structable<R>> Queryable<R> select(final Q.AsMany<? super T, ? extends S> flatMap) {
    return new Selection<>(new Select<>(this, Nullability.nonNullable(flatMap, "flatMap")));
  }

  @Deprecated(forRemoval = true)
  default Queryable<T> peek(final Consumer1<? super T> peek) {
    return new Peek<>(this, Nullability.nonNullable(peek, "peek"));
  }

}

final class Select<T, S> implements Queryable<S> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends S> map;

  @Contract(pure = true)
  Select(final Structable<T> structable, Function1<? super T, ? extends S> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var array = Many.<S>of();
    for (var value : structable) array.add(map.apply(value));
    return array.iterator();
  }
}

final class Selection<R, S extends Structable<R>> implements Queryable<R> {
  private final Structable<S> structables;

  @Contract(pure = true)
  Selection(Structable<S> structables) {
    this.structables = structables;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = Many.<R>of();
    for (final var structable : structables) {
      for (final var value : structable) {
        array.add(value);
      }
    }
    return array.iterator();
  }
}

final class Peek<T> implements Queryable<T> {
  private final Structable<T> structable;
  private final Consumer1<? super T> peek;

  @Contract(pure = true)
  Peek(final Structable<T> structable, final Consumer1<? super T> peek) {
    this.structable = structable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    for (final var value : structable) peek.accept(value);
    return structable.iterator();
  }
}

final class SelectIth<S, R> implements Queryable<R> {
  private final Structable<S> structable;
  private final IntFunction2<? super S, ? extends R> mapIndex;

  @Contract(pure = true)
  SelectIth(final Structable<S> structable, final IntFunction2<? super S, ? extends R> mapIndex) {
    this.structable = structable;
    this.mapIndex = mapIndex;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = Many.<R>of();
    var index = 0;
    for (var iterator = structable.iterator(); iterator.hasNext(); index++) {
      array.add(mapIndex.applyInt(index, iterator.next()));
    }
    return array.iterator();
  }
}

final class SelectTuple2<V, T1, T2, T extends Tuple2<T1, T2>> implements Queryable2<T1, T2> {
  private final Structable<V> structable;
  private final Function1<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple2(final Structable<V> structable, final Function1<? super V, ? extends T> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = Many.<Tuple2<T1, T2>>of();
    for (final var value : structable) result.add(map.apply(value));
    return result.iterator();
  }
}

final class SelectTuple3<V, T1, T2, T3, T extends Tuple3<T1, T2, T3>> implements Queryable3<T1, T2, T3> {
  private final Structable<V> structable;
  private final Function1<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple3(final Structable<V> structable, final Function1<? super V, ? extends T> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = Many.<Tuple3<T1, T2, T3>>of();
    for (final var value : structable) result.add(map.apply(value));
    return result.iterator();
  }
}
