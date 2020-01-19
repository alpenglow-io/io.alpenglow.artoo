package dev.lug.oak.query.many;

import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.IntFunction2;
import dev.lug.oak.query.Q;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.tuple.Queryable2;
import dev.lug.oak.query.tuple.Queryable3;
import dev.lug.oak.query.tuple.Tuple2;
import dev.lug.oak.query.tuple.Tuple3;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(final Q.AsJust<? super T, ? extends R> map) {
    return new Select<>(this, Nullability.nonNullable(map, "map"));
  }

  default <R> Many<R> select(final IntFunction2<? super T, ? extends R> mapIndex) {
    return new SelectIth<>(this, Nullability.nonNullable(mapIndex, "mapIndex"));
  }

  default <T1, T2, U extends Tuple2<T1, T2>> Queryable2<T1, T2> select(final Q.JustAsTuple2<? super T, ? extends U> tuple) {
    return new SelectTuple2<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <T1, T2, T3, U extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select(final Q.JustAsTuple3<? super T, ? extends U> tuple) {
    return new SelectTuple3<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <R, S extends Queryable<R>> Many<R> select(final Q.AsMany<? super T, ? extends S> flatMap) {
    return new Selection<>(new Select<>(this, Nullability.nonNullable(flatMap, "flatMap")));
  }

  @Deprecated(forRemoval = true)
  default Many<T> peek(final Consumer1<? super T> peek) {
    return new Peek<>(this, Nullability.nonNullable(peek, "peek"));
  }

}

final class Select<T, S> implements Many<S> {
  private final Queryable<T> queryable;
  private final Function1<? super T, ? extends S> map;

  @Contract(pure = true)
  Select(final Queryable<T> queryable, Function1<? super T, ? extends S> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var array = new ArrayList<S>();
    for (var value : queryable) array.add(map.apply(value));
    return array.iterator();
  }
}

final class Selection<R, S extends Queryable<R>> implements Many<R> {
  private final Queryable<S> structables;

  @Contract(pure = true)
  Selection(Queryable<S> structables) {
    this.structables = structables;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    for (final var structable : structables) {
      for (final var value : structable) {
        array.add(value);
      }
    }
    return array.iterator();
  }
}

final class Peek<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Consumer1<? super T> peek;

  @Contract(pure = true)
  Peek(final Queryable<T> queryable, final Consumer1<? super T> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    for (final var value : queryable) peek.accept(value);
    return queryable.iterator();
  }
}

final class SelectIth<S, R> implements Many<R> {
  private final Queryable<S> queryable;
  private final IntFunction2<? super S, ? extends R> mapIndex;

  @Contract(pure = true)
  SelectIth(final Queryable<S> queryable, final IntFunction2<? super S, ? extends R> mapIndex) {
    this.queryable = queryable;
    this.mapIndex = mapIndex;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      array.add(mapIndex.applyInt(index, iterator.next()));
    }
    return array.iterator();
  }
}

final class SelectTuple2<V, T1, T2, T extends Tuple2<T1, T2>> implements Queryable2<T1, T2> {
  private final Queryable<V> queryable;
  private final Function1<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple2(final Queryable<V> queryable, final Function1<? super V, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var value : queryable) result.add(map.apply(value));
    return result.iterator();
  }
}

final class SelectTuple3<V, T1, T2, T3, T extends Tuple3<T1, T2, T3>> implements Queryable3<T1, T2, T3> {
  private final Queryable<V> queryable;
  private final Function1<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple3(final Queryable<V> queryable, final Function1<? super V, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = new ArrayList<Tuple3<T1, T2, T3>>();
    for (final var value : queryable) result.add(map.apply(value));
    return result.iterator();
  }
}
