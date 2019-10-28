package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Projectable<T> extends Structable<T> {
  default <R> Nullable<R> select(final Function1<? super T, ? extends R> map) {
    return new Select<>(this, Nullability.nonNullable(map, "map"));
  }

  default <R, M extends Nullable<? extends R>> Nullable<R> selection(final Function1<? super T, M> flatMap) {
    return new Selection<>(new Select<>(this, Nullability.nonNullable(flatMap, "flatMap")));
  }

  default Nullable<T> peek(final Consumer1<? super T> peek) { return new Peek<>(this, Nullability.nonNullable(peek, "peek")); }
}

final class Select<S, R> implements Nullable<R> {
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
    return Cursor.ofNullable(map.apply(structable.iterator().next()));
  }
}

final class Selection<R, S extends Structable<? extends R>> implements Nullable<R> {
  private final Structable<S> structable;

  @Contract(pure = true)
  Selection(Structable<S> structable) {
    this.structable = structable;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    return Cursor.ofNullable(structable.iterator().next().iterator().next());
  }
}

final class Peek<T> implements Nullable<T> {
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
    final var value = structable.iterator().next();
    peek.accept(value);
    return Cursor.ofNullable(value);
  }
}
