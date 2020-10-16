package io.artoo.lance.fetcher.cursor;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.fetcher.routine.Routine;
import io.artoo.lance.func.Func;

public interface Mapper<T> extends Fetcher<T> {
  default <R> Cursor<R> map(final Func.Uni<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }

  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Func.Uni<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

final class Map<T, R> implements Cursor<R> {
  private final Fetcher<T> source;
  private final Func.Uni<? super T, ? extends R> map;

  Map(final Fetcher<T> source, final Func.Uni<? super T, ? extends R> map) {
    this.source = source;
    this.map = map;
  }

  @Override
  public final R fetch() throws Throwable {
    final var fetched = source.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final Func.Uni<? super R, ? extends P> mapAgain) {
    return new Map<>(source, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <P> Cursor<P> invoke(final Routine<R, P> routine) {
    return routine.onFetcher().apply(this);
  }
}

final class Flat<T> implements Cursor<T> {
  private final Flatten<T> flatten = new Flatten<>();
  private final Fetcher<Fetcher<T>> origin;

  Flat(final Fetcher<Fetcher<T>> origin) {this.origin = origin;}

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

  @Override
  public <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return routine.onFetcher().apply(this);
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
