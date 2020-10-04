package io.artoo.lance.fetcher;

public interface Cursor<T> extends Projector<T>, Other<T>, Closer<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> nothing() {
    return (Cursor<T>) Nothing.Default;
  }

  static <T> Cursor<T> maybe(final T element) {
    return element == null ? nothing() : open(element);
  }
}

record Open<T>(T[] elements, Index index) implements Cursor<T> {
  public Open(T[] elements) {
    this(elements, new Index());
  }

  @Override
  public final T fetch() {
    try {
      return elements[index.value()];
    } finally {
      index.inc();
    }
  }
}

enum Nothing implements Cursor<Object> {
  Default;

  @Override
  public Object fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}

