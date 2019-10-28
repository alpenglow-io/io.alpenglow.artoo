package dev.lug.oak.collect;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.*;
import static java.util.Arrays.copyOf;
import static dev.lug.oak.type.Nullability.nonNullable;

public interface Many<T> extends Iterable<T>, Supplier1<T[]> {
  T at(int index);
  Many<T> add(T element);

  @NotNull
  @Contract(pure = true)
  Many<T> addAt(int index, T element);

  long length();

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <S> Many<S> of(S... elements) {
    return new SafeMany<>(copyOf(nonNullable(elements, "elements"), elements.length));
  }

  @NotNull
  static <S> Many<S> empty() {
    return of();
  }
}

final class SafeMany<T> implements Many<T> {
  private final ThreadLocal<T[]> array;

  @Contract(pure = true)
  SafeMany(final T[] array) {
    this(ThreadLocal.withInitial(() -> array));
  }
  @Contract(pure = true)
  private SafeMany(final ThreadLocal<T[]> array) {
    this.array = array;
  }

  @Override
  public T at(int index) {
    if (index >= array.get().length) throw new IndexOutOfBoundsException("index is greater than length - 1");
    return array.get()[index];
  }

  @Override
  @Contract("_ -> this")
  public Many<T> add(T element) {
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
  public Many<T> addAt(int index, T element) {
    if (index > array.get().length + 1) throw new IllegalArgumentException("Index can't point to more than array.length + 1");
    synchronized (array) {
      final var copied = copyOf(array.get(), array.get().length + 1);
      if (copied.length - 1 - index >= 0) arraycopy(copied, index, copied, index + 1, copied.length - 1 - index);
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

  @Contract(value = "null -> false", pure = true)
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final var safeArray = (SafeMany<?>) o;
    return Arrays.equals(array.get(), safeArray.array.get());
  }

  @Override
  public int hashCode() {
    return array != null ? array.hashCode() : 0;
  }

  @Override
  public final T[] get() {
    return this.array.get();
  }
}
