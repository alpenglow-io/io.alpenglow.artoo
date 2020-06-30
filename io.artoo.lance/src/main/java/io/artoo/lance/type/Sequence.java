package io.artoo.lance.type;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.task.Lock;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public interface Sequence<T> extends Queryable<T> {
  @SafeVarargs
  static <T> Sequence<T> of(T... elements) {
    return new Locked<>(Arrays.copyOf(elements, elements.length), Lock.stamped());
  }

  static <T> Sequence<T> empty() {
    return new Locked<>((T[]) new Object[0], Lock.stamped());
  }

  int size();
  default boolean isEmpty() { return size() == 0; }
  default boolean isNotEmpty() { return !isEmpty(); }

  @SuppressWarnings("UnusedReturnValue")
  Sequence<T> concat(T element);
}

final class Locked<T> implements Sequence<T> {
  private T[] array;
  private final Lock lock;

  Locked(final T[] array, final Lock lock) {
    this.array = array;
    this.lock = lock;
  }

  @Override
  public Sequence<T> concat(final T element) {
    if (element != null) {
      lock.write(() -> {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = element;
      });
    }
    return this;
  }

  @Override
  public int size() {
    return array.length;
  }

  @Override
  public Cursor<T> cursor() {
    return lock.read(() -> Cursor.local(array));
  }
}
