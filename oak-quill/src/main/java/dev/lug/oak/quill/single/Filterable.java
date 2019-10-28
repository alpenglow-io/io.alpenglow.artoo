package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.quill.Structable;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.func.pre.Predicate1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Filterable<T> extends Structable<T> {
  default Nullable<T> where(Predicate1<? super T> filter) { return new Where<>(this, Nullability.nonNullable(filter, "filter")); }

  default <R> Nullable<R> ofType(Class<? extends R> type) { return new OfType<>(this, Nullability.nonNullable(type, "type")); }
}

final class Where<T> implements Nullable<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Where(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var value = structable.iterator().next();
    return Cursor.ofNullable(value != null && filter.test(value) ? value : null);
  }
}

final class OfType<T, R> implements Nullable<R> {
  private final Structable<T> structable;
  private final Class<? extends R> type;

  @Contract(pure = true)
  OfType(final Structable<T> structable, final Class<? extends R> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var value = structable.iterator().next();
    return Cursor.ofNullable(value != null && value.getClass().equals(type) ? type.cast(value) : null);
  }
}
