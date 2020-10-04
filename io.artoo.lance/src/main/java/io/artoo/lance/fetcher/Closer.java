package io.artoo.lance.fetcher;

public interface Closer<T> extends Fetcher<T> {
  default Cursor<T> close() {
    return new Close<>(this);
  }

  default Cursor<T> scroll() {
    T element = null;
    while (hasNext())
      element = next();

    return Cursor.maybe(element);
  }
}

record Close<T>(Fetcher<T> fetcher, Shrunk<T> shrunk) implements Cursor<T> {
  Close(Fetcher<T> fetcher) {
    this(fetcher, new Shrunk<>());
  }

  @Override
  public T fetch() {
    return next();
  }

  @Override
  public boolean hasNext() {
    var hasNext = fetcher.hasNext();
    shrunk.fetched = null;
    while (hasNext && shrunk.fetched == null) {
      shrunk.fetched = fetcher.next();
      hasNext = shrunk.fetched != null || fetcher.hasNext();
    }
    return hasNext;
  }

  @Override
  public T next() {
    return shrunk.fetched(fetcher);
  }

  private static final class Shrunk<T> {
    private T fetched;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private T fetched(Fetcher<T> fetcher) {
      if (fetched == null) fetcher.hasNext();
      return fetched;
    }
  }
}

