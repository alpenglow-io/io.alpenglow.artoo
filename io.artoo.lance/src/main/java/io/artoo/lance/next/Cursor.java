package io.artoo.lance.next;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.cursor.Aggregatable;
import io.artoo.lance.next.cursor.Averageable;
import io.artoo.lance.next.cursor.Awaitable;
import io.artoo.lance.next.cursor.Concatenatable;
import io.artoo.lance.next.cursor.Countable;
import io.artoo.lance.next.cursor.Extremable;
import io.artoo.lance.next.cursor.Filterable;
import io.artoo.lance.next.cursor.Otherwise;
import io.artoo.lance.next.cursor.Partitionable;
import io.artoo.lance.next.cursor.Peekable;
import io.artoo.lance.next.cursor.Projectable;
import io.artoo.lance.next.cursor.Quantifiable;
import io.artoo.lance.next.cursor.Settable;
import io.artoo.lance.next.cursor.Sortable;
import io.artoo.lance.next.cursor.Summable;
import io.artoo.lance.next.cursor.Uniquable;
import io.artoo.lance.type.Lazy;

import static io.artoo.lance.type.Lazy.lazy;
import static java.lang.System.out;
import static java.util.concurrent.ForkJoinPool.commonPool;

public interface Cursor<T> extends
  Aggregatable<T>,
  Awaitable<T>,
  Averageable<T>,
  Concatenatable<T>,
  Countable<T>,
  Extremable<T>,
  Filterable<T>,
  Otherwise<T>,
  Partitionable<T>,
  Peekable<T>,
  Projectable<T>,
  Quantifiable<T>,
  Settable<T>,
  Sortable<T>,
  Summable<T>,
  Uniquable<T> {

  static <T> Cursor<T> nothing() {
    return new Nothing<>();
  }

  static <T> Cursor<T> just(final T item) {
    return new Just<>(item);
  }

  static <T> Cursor<T> maybe(final T item) {
    return item == null ? Cursor.nothing() : Cursor.just(item);
  }

  static <T> Cursor<T> tick(final Suppl.Uni<T> callable) {
    return new Tick<>(callable);
  }

  @SafeVarargs
  static <T> Cursor<T> every(final T... items) {
    return new Every<>(items);
  }

  static <T> Cursor<T> later(final Suppl.Uni<Next<T>> next) {
    return new Later<>(next);
  }

  static int heavyTask1() throws InterruptedException {
    Thread.sleep(5_000);
    return 100;
  }

  static int heavyTask2() throws InterruptedException {
    Thread.sleep(4_000);
    return 200;
  }

  static void main(String[] args) {
    Cursor.tick(Cursor::heavyTask1)
      .select(it -> it * 3)
      .await();

    Cursor.tick(Cursor::heavyTask2).await();

    var activeThreadCount = commonPool().getActiveThreadCount();
    while (activeThreadCount > 0) {
      if (commonPool().getActiveThreadCount() != activeThreadCount)
        out.println(activeThreadCount);
      activeThreadCount = commonPool().getActiveThreadCount();
    }
  }
}

final class Just<T> implements Cursor<T> {
  private final T element;
  private boolean hasNext = true;

  Just(final T element) {
    try {
      assert element != null;
      this.element = element;
    } catch (Throwable throwable) {
      throw new IllegalStateException("element can't be null");
    }
  }

  @Override
  public final T fetch() {
    try {
      return element;
    } finally {
      hasNext = false;
    }
  }

  @Override
  public final boolean hasNext() {
    return hasNext;
  }

  @Override
  public final Cursor<T> open() {
    return new Just<>(element);
  }
}

final class Every<T> implements Cursor<T> {
  private final NonNull nonNull = new NonNull();

  private final T[] elements;

  Every(final T[] elements) {this.elements = elements;}

  @Override
  public T fetch() {
    try {
      if (nonNull.value != null || hasNext()) {
        return nonNull.value;
      }
    } finally {
      nonNull.value = null;
      nonNull.index++;
    }
    return null;
  }

  @Override
  public T[] fetchAll() {
    return elements;
  }

  @Override
  public boolean hasNext() {
    do {
      if (nonNull.index < elements.length && nonNull.value == null) {
        nonNull.value = elements[nonNull.index];
      }
    } while (nonNull.value == null && ++nonNull.index < elements.length);

    return nonNull.value != null;
  }

  @Override
  public final Cursor<T> open() {
    return new Every<>(elements);
  }

  private final class NonNull {
    private T value;
    private int index = 0;
  }
}

final class Tick<T> implements Cursor<T> {
  private final Suppl.Uni<T> callable;

  Tick(final Suppl.Uni<T> callable) {this.callable = callable;}

  @Override
  public T fetch() throws Throwable {
    return callable.call();
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}

final class Later<T> implements Cursor<T> {
  private final Lazy<Next<T>> next;

  Later(final Suppl.Uni<Next<T>> next) {
    assert next != null;
    this.next = lazy(next);
  }

  @Override
  public T[] fetchAll() throws Throwable {
    return next.tryGet().fetchAll();
  }

  @Override
  public T fetch() throws Throwable {
    return next.tryGet().fetch();
  }

  @Override
  public T next() {
    return next.get().next();
  }

  @Override
  public boolean hasNext() {
    return next.get().hasNext();
  }
}

final class Nothing<T> implements Cursor<T> {
  @Override
  public T fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public T next() {
    return null;
  }
}
