package io.artoo.lance.next;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public interface Queue<T> extends Next<T> {
  @Override
  default T fetch() throws Throwable {
    return peek().fetch();
  }

  @Override
  default boolean hasNext() {
    return peek().hasNext();
  }

  @Override
  default T next() {
    return peek().next();
  }

  boolean isEmpty();

  default boolean isNotEmpty() { return !isEmpty(); }

  <N extends Next<T>> Queue<T> attach(N next);

  Next<T> detach();

  Next<T> peek();

  @SafeVarargs
  static <T> Queue<T> each(final Next<T>... next) {
    return new Each<>(next);
  }
}

final class Each<T> implements Queue<T> {
  private Next<T>[] elements;

  Each(final Next<T>[] elements) {
    this.elements = elements;
  }

  @Override
  public boolean isEmpty() {
    return elements.length == 0;
  }

  @Override
  public <N extends Next<T>> Queue<T> attach(final N element) {
    elements = Arrays.copyOf(elements, elements.length + 1);
    elements[elements.length - 1] = element;
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Next<T> detach() {
    if (isNotEmpty()) {
      final var element = elements[0];
      var items = new Next[elements.length - 1];
      arraycopy(elements, 1, items, 0, items.length);
      elements = (Next<T>[]) items;
      return element;
    }
    return null;
  }

  @Override
  public Next<T> peek() {
    return isNotEmpty() ? elements[0] : null;
  }
}
