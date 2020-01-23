package dev.lug.oak.query.one;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.query.Q;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Q.AsJust<? super T, ? extends R> map) {
    return new Select<>(this, Nullability.nonNullable(map, "map"));
  }

  default <R, M extends One<? extends R>> One<R> select(final Function1<? super T, M> flatMap) {
    return new Selection<>(new Select<>(this, Nullability.nonNullable(flatMap, "flatMap")));
  }

  default One<T> peek(final Consumer1<? super T> peek) { return new Peek<>(this, Nullability.nonNullable(peek, "peek")); }
}

final class Select<S, R> implements One<R> {
  private final Queryable<S> queryable;
  private final Function1<? super S, ? extends R> map;

  @Contract(pure = true)
  Select(final Queryable<S> queryable, Function1<? super S, ? extends R> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    return Cursor.ofNullable(map.apply(queryable.iterator().next()));
  }
}

final class Selection<R, S extends Queryable<? extends R>> implements One<R> {
  private final Queryable<S> queryable;

  @Contract(pure = true)
  Selection(Queryable<S> queryable) {
    this.queryable = queryable;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    return Cursor.ofNullable(queryable.iterator().next().iterator().next());
  }
}

final class Peek<T> implements One<T> {
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
    final var value = queryable.iterator().next();
    peek.accept(value);
    return Cursor.ofNullable(value);
  }
}
