package oak.collect.query;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public interface ElementArray<T> extends Maybe<T> {
  static <S> Maybe<S> at(final QueryableArray<S> array, final int index) {
    return new ArrayAt<>(
      requireNonNull(array, "Some is null"),
      index
    );
  }
}

final class ArrayAt<T> implements ElementArray<T> {
  private final QueryableArray<T> array;
  private final int index;

  @Contract(pure = true)
  ArrayAt(final QueryableArray<T> array, final int index) {
    this.array = array;
    this.index = index;
  }

  @Override
  @NotNull
  public Iterator<T> iterator() {
    return array.get().length > index ? Cursor.once(array.get()[index]) : Cursor.none();
  }
}
