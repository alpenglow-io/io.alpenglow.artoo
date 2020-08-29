package io.artoo.lance.next;

import io.artoo.lance.func.Cons;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public interface Next<T> extends Iterator<T> {
  default T[] fetchAll() throws Throwable {
    return (T[]) (hasNext() ? new Object[] { fetch() } : new Object[0]);
  }
  T fetch() throws Throwable;

  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  @Override
  default boolean hasNext() {
    return false;
  }
}
