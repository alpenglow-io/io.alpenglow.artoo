package oak.query.many;

import oak.func.Con;
import oak.func.Func;
import oak.func.fun.IntFunction2;
import oak.query.Queryable;
import oak.query.many.$2.Many;
import oak.query.$3.Queryable3;
import dev.lug.oak.query.tuple3.Tuple3;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Projectable<T> extends Queryable<T> {
  default <R> oak.query.Many<R> select(final AnyAsAny<? super T, ? extends R> map) {
    return new Select<>(this, Nullability.nonNullable(map, "map"));
  }

  default <R> oak.query.Many select(final IntFunction2<? super T, ? extends R> mapIndex) {
    return new SelectIth<>(this, Nullability.nonNullable(mapIndex, "mapIndex"));
  }

  default <T1, T2, U extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> Many<T1, T2> select(final AnyAsTuple<? super T, ? extends U> tuple) {
    return new SelectTuple<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <T1, T2, T3, U extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select(final JustAsTuple3<? super T, ? extends U> tuple) {
    return new SelectTuple3<>(this, Nullability.nonNullable(tuple, "tuple"));
  }

  default <R, S extends Queryable<R>> oak.query.Many select(final AnyAsQueryable<? super T, ? super R, ? extends S> flatMap) {
    return new Selection<>(new Select<>(this, Nullability.nonNullable(flatMap, "flatMap")));
  }

  @Deprecated(forRemoval = true)
  default oak.query.Many peek(final Con<? super T> peek) {
    return new Peek<>(this, Nullability.nonNullable(peek, "peek"));
  }

}

final class Select<T, S> implements oak.query.Many {
  private final Queryable<T> queryable;
  private final Func<? super T, ? extends S> map;

  @Contract(pure = true)
  Select(final Queryable<T> queryable, Func<? super T, ? extends S> map) {
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

final class Selection<R, S extends Queryable<R>> implements oak.query.Many {
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

final class Peek<T> implements oak.query.Many {
  private final Queryable<T> queryable;
  private final Con<? super T> peek;

  @Contract(pure = true)
  Peek(final Queryable<T> queryable, final Con<? super T> peek) {
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

final class SelectIth<S, R> implements oak.query.Many {
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

final class SelectTuple<V, T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> implements Many<T1, T2> {
  private final Queryable<V> queryable;
  private final Func<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple(final Queryable<V> queryable, final Func<? super V, ? extends T> map) {
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
  private final Func<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple3(final Queryable<V> queryable, final Func<? super V, ? extends T> map) {
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
