package oak.collect;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Arrays.copyOf;
import static oak.type.Nullability.nonNullable;

public interface Array<T> extends Iterable<T> {
  Array<T> add(T element);

  @NotNull
  @Contract(pure = true)
  Array<T> addAt(int index, T element);

  long length();

  @SafeVarargs
  static <S> Array<S> of(S... elements) {
    return new SafeArray<>(copyOf(nonNullable(elements, "elements"), elements.length));
  }
}

final class SafeArray<T> implements Array<T> {
  private final ThreadLocal<T[]> array;

  @Contract(pure = true)
  SafeArray(final T[] array) {
    this(ThreadLocal.withInitial(() -> array));
  }
  @Contract(pure = true)
  private SafeArray(final ThreadLocal<T[]> array) {
    this.array = array;
  }

  @Override
  @Contract("_ -> this")
  public Array<T> add(T element) {
    synchronized (array) {
      final var copied = copyOf(array.get(), array.get().length + 1);
      copied[array.get().length] = element;
      array.set(copied);
    }
    return this;
  }

  @Override
  @NotNull
  @Contract(pure = true)
  public Array<T> addAt(int index, T element) {
    if (index > array.get().length + 1) throw new IllegalArgumentException("Index can't point to more than array.length + 1");
    synchronized (array) {
      final var copied = copyOf(array.get(), array.get().length + 1);
      for (var i = copied.length - 1; i > index; i--) {
        copied[i] = copied[i - 1];
      }
      copied[index] = element;
      array.set(copied);
    }
    return this;
  }

  @Override
  @Contract(pure = true)
  public long length() {
    return array.get().length;
  }

  @Override
  @NotNull
  public final Iterator<T> iterator() {
    return Cursor.of(array.get());
  }
}
