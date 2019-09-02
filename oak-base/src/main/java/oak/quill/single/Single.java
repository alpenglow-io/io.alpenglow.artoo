package oak.quill.single;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Single<T> extends Nullable<T> {
  @NotNull
  @Contract("_ -> new")
  static <S> Single<S> of(final S value) {
    return new Some<>(nonNullable(value, "value"));
  }
}

final class Some<T> implements Single<T> {
  private final T value;

  @Contract(pure = true)
  Some(final T value) {
    this.value = value;
  }

  @Override
  @NotNull
  public final Iterator<T> iterator() {
    return Cursor.of(value);
  }
}
