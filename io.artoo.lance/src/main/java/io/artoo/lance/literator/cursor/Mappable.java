package io.artoo.lance.literator.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;

public interface Mappable<T> extends Literator<T> {
  default <R> Cursor<R> map(final Func.MaybeFunction<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }

  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Func.MaybeFunction<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

final class Map<T, R> implements Cursor<R> {
  private final Literator<T> source;
  private final Func.MaybeFunction<? super T, ? extends R> map;

  Map(final Literator<T> source, final Func.MaybeFunction<? super T, ? extends R> map) {
    this.source = source;
    this.map = map;
  }

  @Override
  public final R fetch() throws Throwable {
    final var fetched = source.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final Func.MaybeFunction<? super R, ? extends P> mapAgain) {
    return new Map<>(source, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<R, R1> routine) {
    return routine.onLiterator().apply(this);
  }
}

final class Flat<T> implements Cursor<T> {
  private final Flatten<T> flatten = new Flatten<>();
  private final Literator<Literator<T>> origin;

  Flat(final Literator<Literator<T>> origin) {this.origin = origin;}

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public final T fetch() throws Throwable {
    T element = null;
    while (hasNext() && (element = flatten.literator().fetch()) == null)
      ;
    return element;
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext(origin.hasNext() || (flatten.literator() != null && flatten.literator().hasNext()));

    if (flatten.hasNext() && (flatten.literator() == null || !flatten.literator().hasNext())) {
      flatten.literator(origin.next());
    }

    return flatten.hasNext();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onLiterator().apply(this);
  }
}

final class Flatten<T> {
  private boolean hasNext = true;
  private Literator<T> literator;
  private final Object lock = new Object() {};

  public final Flatten<T> hasNext(boolean hasNext) {
    synchronized (lock) {
      this.hasNext = hasNext;
    }
    return this;
  }

  public final boolean hasNext() { return hasNext; }

  public final Flatten<T> literator(Literator<T> literator) {
    synchronized (lock) {
      this.literator = literator;
    }
    return this;
  }

  public final Literator<T> literator() { return literator; }
}
