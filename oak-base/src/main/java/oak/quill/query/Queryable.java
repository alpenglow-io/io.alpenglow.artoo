package oak.quill.query;

import oak.collect.Array;
import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Queryable<T> extends
  Projectable<T>,
  Filterable<T>,
  Partitionable<T>,
  Uniquable<T>,
  Aggregatable<T>,
  Concatenatable<T>,
  Groupable<T>,
  Joinable<T>,
  Quantifiable<T>
{
  @NotNull
  @Contract(value = " -> new", pure = true)
  static <S> Queryable<S> empty() {
    return new Empty<>();
  }

  @NotNull
  @Contract("_, _ -> new")
  static <S> Queryable<S> repeat(final Supplier1<? extends S> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }
}

final class Empty<Q> implements Queryable<Q> {
  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<Q> iterator() {
    return Cursor.none();
  }
}

final class Repeat<T> implements Queryable<T> {
  private final Supplier1<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  Repeat(final Supplier1<? extends T> supplier, final int count) {
    this.supplier = supplier;
    this.count = count;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = Array.<T>of();
    for (var index = 0; index < count; index++) {
      array.add(supplier.get());
    }
    return array.iterator();
  }
}
