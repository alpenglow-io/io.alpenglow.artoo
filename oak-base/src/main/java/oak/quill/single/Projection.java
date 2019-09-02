package oak.quill.single;

import oak.collect.cursor.Cursor;
import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Projection<T> extends Structable<T> {
  default <R> Nullable<R> select(final Function1<? super T, ? extends R> map) {
    return new Select<>(this, nonNullable(map, "map"));
  }

  default <R, M extends Nullable<? extends R>> Nullable<R> selection(final Function1<? super T, M> flatMap) {
    return new Selection<>(new Select<>(this, nonNullable(flatMap, "flatMap")));
  }

  default Nullable<T> peek(final Consumer1<? super T> peek) { return new Peek<>(this, nonNullable(peek, "peek")); }
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
    return Cursor.ofSingle(structable, map);
  }
}

final class Selection<R, S extends Structable<? extends R>> implements Nullable<R> {
  private final Structable<S> structables;

  @Contract(pure = true)
  Selection(Structable<S> structables) {
    this.structables = structables;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var cursor1 = structables.iterator();
    final var structable = cursor1.hasNext() ? cursor1.next() : null;
    final var cursor2 = structable != null ? structable.iterator() : null;
    return Cursor.ofNullable(cursor2 != null ? cursor2.next() : null);
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
    final var cursor = structable.iterator();
    var value = cursor.hasNext() ? cursor.next() : null;
    if (value != null) peek.accept(value);
    return Cursor.ofNullable(value);
  }
}
