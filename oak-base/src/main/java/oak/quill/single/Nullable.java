package oak.quill.single;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.isNull;

public interface Nullable<T> extends Projection<T>, Filtering<T> {
  static <L> Nullable<L> of(final L value) {
    return isNull(value) ? Nullable.none() : Single.of(value);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <L> Nullable<L> none() {
    return new None<>();
  }
}

final class None<T> implements Nullable<T> {
  @Override
  @NotNull
  @Contract(pure = true)
  public final Iterator<T> iterator() {
    return Cursor.none();
  }
}
