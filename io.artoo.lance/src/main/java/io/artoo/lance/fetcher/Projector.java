package io.artoo.lance.fetcher;

import io.artoo.lance.func.Func;

public interface Projector<T> extends Fetcher<T> {
  default <R> Cursor<R> map(final Func.Uni<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }

  default <R> Cursor<R> mapArray(final Func.Uni<? super T[], ? extends R[]> map) {
    return Cursor.open(map.apply(fetcher().elements()));
  }

  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Func.Uni<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

record Map<T, R>(Fetcher<T> origin, Func.Uni<? super T, ? extends R> map) implements Cursor<R> {
  public Map { assert origin != null && map != null; }

  @Override
  public final R fetch() throws Throwable {
    final var fetched = origin.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final Func.Uni<? super R, ? extends P> mapAgain) {
    return new Map<>(origin, map.then(mapAgain));
  }
}

record Flat<T>(Fetcher<Fetcher<T>> origin, Flatten<T> flatten) implements Cursor<T> {
  Flat { assert origin != null && flatten != null; }
  Flat(final Fetcher<Fetcher<T>> fetcher) {
    this(fetcher, new Flatten<>());
    assert fetcher != null;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public final T fetch() throws Throwable {
    T element = null;
    while (hasNext() && (element = flatten.fetcher().fetch()) == null)
      ;
    return element;
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext(origin.hasNext() || (flatten.fetcher() != null && flatten.fetcher().hasNext()));

    if (flatten.hasNext() && (flatten.fetcher() == null || !flatten.fetcher().hasNext())) {
      flatten.fetcher(origin.next());
    }

    return flatten.hasNext();
  }
}

final class Flatten<T> {
  private boolean hasNext = true;
  private Fetcher<T> fetcher;
  private final Object lock = new Object() {};

  public final Flatten<T> hasNext(boolean hasNext) {
    synchronized (lock) {
      this.hasNext = hasNext;
    }
    return this;
  }

  public final boolean hasNext() { return hasNext; }

  public final Flatten<T> fetcher(Fetcher<T> fetcher) {
    synchronized (lock) {
      this.fetcher = fetcher;
    }
    return this;
  }

  public final Fetcher<T> fetcher() { return fetcher; }
}
