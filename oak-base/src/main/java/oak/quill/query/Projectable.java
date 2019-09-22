package oak.quill.query;

import oak.collect.Many;
import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.fun.IntFunction2;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

interface Projectable<T> extends Structable<T> {
  default <R> Queryable<R> select(final Function1<? super T, ? extends R> map) {
    return new Select<>(this, nonNullable(map, "map"));
  }
  default <R, S extends Structable<? extends R>> Queryable<R> selection(final Function1<? super T, ? extends S> flatMap) {
    return asQueryable(new Selection<>(new Select<>(this, nonNullable(flatMap, "flatMap"))));
  }
  default Queryable<T> peek(final Consumer1<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"));
  }
  default <R> Queryable<R> select(final IntFunction2<? super T, ? extends R> mapIndex) {
    return new SelectIndex<>(this, nonNullable(mapIndex, "mapIndex"));
  }

  @NotNull
  @Contract(pure = true)
  private <R> Queryable<R> asQueryable(final Structable<R> structable) {
    return () -> nonNullable(structable, "structable").iterator();
  }
}

final class Select<S, R> implements Queryable<R> {
  private final Structable<S> structable;
  private final Function1<? super S, ? extends R> map;

  @Contract(pure = true)
  Select(final Structable<S> structable, Function1<? super S, ? extends R> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = Many.<R>of();
    for (var value : structable) array.add(map.apply(value));
    return array.iterator();
  }
}

final class Selection<R, S extends Structable<? extends R>> implements Structable<R> {
  private final Structable<S> structables;

  @Contract(pure = true)
  Selection(Structable<S> structables) {
    this.structables = structables;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = Many.<R>of();
    for (var structable : structables) {
      for (R value : structable) {
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

final class SelectIndex<S, R> implements Queryable<R> {
  private final Structable<S> structable;
  private final IntFunction2<? super S, ? extends R> mapIndex;

  @Contract(pure = true)
  SelectIndex(final Structable<S> structable, final IntFunction2<? super S, ? extends R> mapIndex) {
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
