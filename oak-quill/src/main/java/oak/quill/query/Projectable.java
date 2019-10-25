package oak.quill.query;

import oak.collect.Many;
import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.fun.IntFunction2;
import oak.quill.Q.AsJust;
import oak.quill.Q.AsMany;
import oak.quill.Q.JustAsTuple2;
import oak.quill.Q.JustAsTuple3;
import oak.quill.Structable;
import oak.quill.query.tuple.Queryable2;
import oak.quill.query.tuple.Queryable3;
import oak.quill.tuple.Tuple2;
import oak.quill.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Structable<T> {
  default <R> Queryable<R> select(final AsJust<? super T, ? extends R> map) {
    return new Select<>(this, nonNullable(map, "map"));
  }

  default <R> Queryable<R> select(final IntFunction2<? super T, ? extends R> mapIndex) {
    return new SelectIth<>(this, nonNullable(mapIndex, "mapIndex"));
  }

  default <T1, T2, U extends Tuple2<T1, T2>> Queryable2<T1, T2> select(final JustAsTuple2<? super T, ? extends U> tuple) {
    return new SelectTuple2<>(this, nonNullable(tuple, "tuple"));
  }

  default <T1, T2, T3, U extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select(final JustAsTuple3<? super T, ? extends U> tuple) {
    return new SelectTuple3<>(this, nonNullable(tuple, "tuple"));
  }

  default <R, S extends Structable<R>> Queryable<R> select(final AsMany<? super T, ? extends S> flatMap) {
    return new Selection<>(new Select<>(this, nonNullable(flatMap, "flatMap")));
  }

  @Deprecated(forRemoval = true)
  default Queryable<T> peek(final Consumer1<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"));
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
