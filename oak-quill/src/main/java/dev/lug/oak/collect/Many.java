package dev.lug.oak.collect;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.sup.Supplier1;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.one.One;
import dev.lug.oak.type.Lazy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Objects.nonNull;

@SuppressWarnings("unused")
public interface Many<T> extends Structable<T> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <S> Many<S> of(S... elements) {
    return new SyncMany<>(copyOf(nonNullable(elements, "elements"), elements.length));
  }

  @NotNull
  static <S> Many<S> none() {
    return of();
  }

  One<T> at(int index);

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

final class Element<T> {
  final T value;
  Element<T> next;

  Element(final T value) {
    this(value, null);
  }
  Element(final T value, final Element<T> next) {
    this.value = value;
    this.next = next;
  }
}

final class SafeMany<T> implements Many<T> {
  private Element<T> first;
  private Element<T> last;
  private long length;
  private final StampedLock stamp;

  SafeMany(final Element<T> first, final Element<T> last, final long length, final StampedLock stamp) {
    this.first = first;
    this.last = last;
    this.length = length;
    this.stamp = stamp;
  }

  @Override
  public final Many<T> add(final T value) {
    final var write = this.stamp.asWriteLock();
    write.lock();
    try {
      this.last.next = new Element<>(value);
      this.last = this.last.next;
      this.length++;
      return this;
    } finally {
      write.unlock();
    }
  }

  @Override
  public Many<T> addAt(int index, T value) {
    final var write = this.stamp.asWriteLock();
    write.lock();

    try {
      var current = this.first;
      var cursor = 0;
      while (cursor < index && nonNull(current.next)) current = current.next;
      current.next = new Element<>(value, current.next);
      return this;
    } finally {
      write.unlock();
    }
  }

  @Override
  public One<T> at(int index) {
    return null;
  }
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
  public final One<T> at(int index) {
    if (index >= array.get().length) throw new IndexOutOfBoundsException("index is greater than length - 1");
    return One.of(array.get()[index]);
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
