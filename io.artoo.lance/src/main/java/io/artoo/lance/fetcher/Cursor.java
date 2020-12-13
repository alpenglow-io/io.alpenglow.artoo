package io.artoo.lance.fetcher;

import io.artoo.lance.fetcher.cursor.Closer;
import io.artoo.lance.fetcher.cursor.Invoker;
import io.artoo.lance.fetcher.cursor.Mapper;
import io.artoo.lance.fetcher.cursor.Other;
import io.artoo.lance.fetcher.routine.Routine;

import java.util.Iterator;

public interface Cursor<T> extends Mapper<T>, Other<T>, Closer<T>, Invoker<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> link(final Fetcher<T> prev, final Fetcher<T> next) {
    return new Link<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> nothing() {
    return (Cursor<T>) Nothing.Default;
  }

  static <T> Cursor<T> maybe(final T element) {
    return element == null ? nothing() : open(element);
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }
}

final class Iteration<T> implements Cursor<T> {
  private final Iterator<T> iterator;

  Iteration(final Iterator<T> iterator) {this.iterator = iterator;}

  @Override
  public T fetch() throws Throwable {
    return iterator.next();
  }

  @Override
  public <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return routine.onIterator().apply(iterator);
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }
}

final class Open<T> implements Cursor<T> {
  private final Index index = Index.incremental();
  private final T[] elements;

  Open(final T[] elements) {this.elements = elements;}

  @Override
  public final T fetch() {
    try {
      return elements[index.value()];
    } finally {
      index.inc();
    }
  }

  @Override
  public boolean hasNext() {
    var hasNext = index.value() < elements.length;
    while (hasNext && elements[index.value()] == null) {
      index.inc();
      hasNext = index.value() < elements.length;
    }
    return hasNext;
  }

  @Override
  public <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return routine.onArray().apply(elements);
  }
}

final class Link<T> implements Cursor<T> {
  private final Fetcher<T> prev;
  private final Fetcher<T> next;

  Link(final Fetcher<T> prev, final Fetcher<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return routine.onFetcher().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : next.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
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

  @Override
  public <R> Cursor<R> invoke(final Routine<Object, R> routine) {
    return Cursor.nothing();
  }
}
