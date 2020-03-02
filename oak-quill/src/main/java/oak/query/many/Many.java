package oak.query.many;

import oak.cursor.Cursor;
import oak.func.Suppl;
import oak.query.Repeat;
import oak.query.one.Either;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Many<T> extends
  Projectable<T>, Filterable<T>, Partitionable<T>, Uniquable<T>, Aggregatable<T>, Concatenatable<T>, Groupable<T>,
  Joinable<T>, Quantifiable<T>, Settable<T>, Insertable<T>, Either<T> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T> Many<T> from(final T... items) {
    return new Array<>(Arrays.copyOf(items, items.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Many<T> from(final Iterable<T> iterable) {
    return new Array<>(nonNullable(iterable, "iterable"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <T> Many<T> none() {
    return (Many<T>) Default.Empty;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <S> Many<S> repeat(final Suppl<? extends S> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }

}

final class Array<T> implements Many<T> {
  private final T[] values;

  @SafeVarargs
  @Contract(pure = true)
  Array(final T... values) {
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.many(values);
  }
}
