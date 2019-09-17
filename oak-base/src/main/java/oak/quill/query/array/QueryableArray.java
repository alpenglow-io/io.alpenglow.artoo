package oak.quill.query.array;

import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;
import oak.quill.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface QueryableArray<Q> extends UniquableArray<Q> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T> Queryable<T> from(final T... elements) {
    return () -> new QueriedArray<>(Arrays.copyOf(nonNullable(elements, "elements"), elements.length)).iterator();
  }
}

final class QueriedArray<T> implements QueryableArray<T> {
  private final T[] elements;

  @Contract(pure = true)
  QueriedArray(final T[] elements) {
    this.elements = elements;
  }

  @Override
  @Contract(pure = true)
  public final T[] get() {
    return elements;
  }
}
