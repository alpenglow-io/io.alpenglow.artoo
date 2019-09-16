package oak.quill.single;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public interface Nullable<T> extends ProjectableSingle<T>, FilterableSingle<T>, CasingSingle<T> {
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
  @Contract(pure = true)
  @org.jetbrains.annotations.Nullable
  public final T get() {
    return null;
  }
}
