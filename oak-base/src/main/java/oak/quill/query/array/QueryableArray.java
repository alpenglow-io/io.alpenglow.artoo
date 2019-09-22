package oak.quill.query.array;

import oak.quill.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static oak.type.Nullability.nonNullable;

public interface QueryableArray<Q> extends Queryable<Q>, UniquableArray<Q> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T> Queryable<T> from(final T... elements) {
    return new QueriedArray<>(Arrays.copyOf(nonNullable(elements, "elements"), elements.length));
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
