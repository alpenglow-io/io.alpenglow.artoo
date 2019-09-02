package oak.quill.single;

import oak.collect.cursor.Cursor;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Filtering<T> extends Structable<T> {
  default Nullable<T> where(Predicate1<? super T> filter) { return new Where<>(this, nonNullable(filter, "filter")); }

  default <R> Nullable<R> ofType(Class<? extends R> type) { return new OfType<>(this, nonNullable(type, "type")); }
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
    return value != null && filter.test(value) ? Cursor.of(value) : Cursor.none();
  }
}

final class OfType<T, R> implements Nullable<R> {
  private final Structable<T> structable;
  private final Class<? extends R> type;

  OfType(final Structable<T> structable, final Class<? extends R> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var value = structable.iterator().next();
    return value != null && value.getClass().equals(type) ? Cursor.of(type.cast(value)) : Cursor.none();
  }
}
