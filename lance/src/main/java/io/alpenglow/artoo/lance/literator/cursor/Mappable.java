package io.alpenglow.artoo.lance.literator.cursor;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;
import io.alpenglow.artoo.lance.literator.cursor.routine.Routine;

public interface Mappable<T> extends Pointer<T> {
  default <R> Cursor<R> map(final TryFunction1<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }

  default <R, C extends Cursor<R>> Cursor<R> flatMap(final TryFunction1<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

final class Map<T, R> implements Cursor<R> {
  private final Pointer<T> source;
  private final TryFunction1<? super T, ? extends R> map;

  Map(final Pointer<T> source, final TryFunction1<? super T, ? extends R> map) {
    this.source = source;
    this.map = map;
  }

  @Override
  public R fetch() throws Throwable {
    final var fetched = source.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final TryFunction1<? super R, ? extends P> mapAgain) {
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
  private final Pointer<Pointer<T>> origin;

  Flat(final Pointer<Pointer<T>> origin) {this.origin = origin;}

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public T fetch() throws Throwable {
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
  private Pointer<T> pointer;
  private final Object lock = new Object();

  public Flatten<T> hasNext(boolean hasNext) {
    synchronized (lock) {
      this.hasNext = hasNext;
    }
    return this;
  }

  public boolean hasNext() { return hasNext; }

  public Flatten<T> literator(Pointer<T> pointer) {
    synchronized (lock) {
      this.pointer = pointer;
    }
    return this;
  }

  public Pointer<T> literator() { return pointer; }
}
