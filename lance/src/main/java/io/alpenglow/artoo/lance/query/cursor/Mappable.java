package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Repeatable;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public interface Mappable<OLD> extends Repeatable<OLD> {
  default <NEW> Cursor<NEW> map(final TryFunction1<? super OLD, ? extends NEW> map) {
    return new Map<>(this, map);
  }

  default <NEW, CURSOR extends Cursor<NEW>> Cursor<NEW> flatMap(final TryFunction1<? super OLD, ? extends CURSOR> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

final class Map<OLD, NEW> implements Cursor<NEW> {
  private final Repeatable<OLD> source;
  private final TryFunction1<? super OLD, ? extends NEW> map;

  Map(final Repeatable<OLD> source, final TryFunction1<? super OLD, ? extends NEW> map) {
    this.source = source;
    this.map = map;
  }

  @Override
  public NEW fetch() throws Throwable {
    final var fetched = source.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final TryFunction1<? super NEW, ? extends P> mapAgain) {
    return new Map<>(source, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<NEW, R1> routine) {
    return routine.onLiterator().apply(this);
  }
}

final class Flat<T> implements Cursor<T> {
  private final Flatten<T> flatten = new Flatten<>();
  private final Repeatable<Repeatable<T>> origin;

  Flat(final Repeatable<Repeatable<T>> origin) {this.origin = origin;}

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
  private Repeatable<T> repeatable;
  private final Object lock = new Object();

  public Flatten<T> hasNext(boolean hasNext) {
    synchronized (lock) {
      this.hasNext = hasNext;
    }
    return this;
  }

  public boolean hasNext() { return hasNext; }

  public Flatten<T> literator(Repeatable<T> repeatable) {
    synchronized (lock) {
      this.repeatable = repeatable;
    }
    return this;
  }

  public Repeatable<T> literator() { return repeatable; }
}
