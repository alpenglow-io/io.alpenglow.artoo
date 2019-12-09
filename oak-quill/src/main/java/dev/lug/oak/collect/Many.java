package dev.lug.oak.collect;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.sup.Supplier1;
import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.type.Lazy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

@SuppressWarnings("unused")
public interface Many<T> extends Iterable<T> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <S> Many<S> of(S... elements) {
    return new SyncMany<>(copyOf(nonNullable(elements, "elements"), elements.length));
  }

  @NotNull
  static <S> Many<S> empty() {
    return of();
  }

  Nullable<T> at(int index);

  Many<T> add(T value);
  Many<T> addAt(int index, T value);
  Many<T> remove(T value);
  Many<T> removeAt(int index);

  int size();

  boolean has(T element);
  default boolean hasNot(T element) {
    return !has(element);
  }
  int indexOf(T element);

  default boolean isEmpty() { return size() == 0; }
  default boolean isNotEmpty() { return size() > 0; }

  //Collection<T> asCollection();
}

final class SyncMany<T> implements Many<T> {
  private final Lazy<T[]> array;

  @SafeVarargs
  @Contract(pure = true)
  SyncMany(final T... array) {
    this(() -> array);
  }
  @Contract(pure = true)
  private SyncMany(Supplier1<T[]> attacher) {
    this.array = Lazy.of(attacher);
  }

  @Override
  public final Nullable<T> at(int index) {
    if (index >= array.get().length) throw new IndexOutOfBoundsException("index is greater than length - 1");
    return Nullable.of(array.get()[index]);
  }

  @Override
  @Contract("_ -> this")
  public Many<T> add(T value) {
    synchronized (this) {
      array
        .release()
        .attach(() -> {
          final var copied = copyOf(array.get(), array.get().length + 1);
          copied[array.get().length] = value;
          return copied;
        });
    }
    return this;
  }

  @Override
  @NotNull
  @Contract(pure = true)
  public final Many<T> addAt(int index, T value) {
    if (index > array.get().length + 1)
      throw new IllegalArgumentException("Index can't point to more than array.length + 1");
    synchronized (this) {
      final var copied = copyOf(array.get(), array.get().length + 1);
      if (copied.length - 1 - index >= 0) arraycopy(copied, index, copied, index + 1, copied.length - 1 - index);
      copied[index] = value;
      array
        .release()
        .attach(() -> copied);
      return this;
    }
  }

  @Override
  @Contract("_ -> this")
  public final Many<T> remove(final T value) {
    return removeAt(indexOf(nonNullable(value, "value")));
  }

  @Override
  @Contract("_ -> this")
  public final Many<T> removeAt(final int index) {
    synchronized (this) {
      final var arry = this.array.get();
      final var length = arry.length;
      if (index > -1 && index < length) {
        final var copied = copyOf(arry, length - 1);
        if (length - 1 - index >= 0) System.arraycopy(arry, index + 1, copied, index, length - 1 - index);
        this.array
          .release()
          .attach(() -> copied);
      }
      return this;
    }
  }

  @Override
  @Contract(pure = true)
  public final int size() {
    return array.get().length;
  }

  @Override
  @Contract(pure = true)
  public final boolean has(final T element) {
    final var indexOf = indexOf(element);
    return indexOf > -1 && indexOf < this.array.get().length;
  }

  @Override
  public final int indexOf(final T element) {
    synchronized (this) {
      var found = false;
      var arry = array.get();
      var index = -1;
      var length = arry.length;
      while (++index < length && !found) {
        found = arry[index].equals(element);
      }
      return index;
    }
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
    final var sync = (SyncMany<?>) o;
    return Arrays.equals(array.get(), sync.array.get());
  }

  @Override
  public int hashCode() {
    return array != null ? array.hashCode() : 0;
  }
}
