package io.artoo.lance.literator.cursor.impl;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.cursor.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.scope.Late;
import io.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Other<T> extends Literator<T> {
  default <C extends Cursor<T>> Cursor<T> or(final Suppl.Uni<? extends C> alternative) {
    return new Or<>(this, Let.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Bi<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

final class Or<T, C extends Cursor<T>> implements Cursor<T> {
  private final Late<Literator<T>> reference = Late.init();
  private final Literator<T> source;
  private final Let<? extends C> other;

  Or(final Literator<T> source, final Let<? extends C> other) {
    this.source = source;
    this.other = other;
  }

  @Override
  public final T fetch() throws Throwable {
    return hasNext() ? reference.let(Literator::fetch) : null;
  }

  @Override
  public final boolean hasNext() {
    other.get(otherwise -> reference.set(() -> source.hasNext() ? source : otherwise));

    return reference.let(Iterator::hasNext);
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>nothing().as(routine);
  }
}

final class Er<T, E extends RuntimeException> implements Cursor<T> {
  private final Literator<T> source;
  private final String message;
  private final Func.Bi<? super String, ? super Throwable, ? extends E> exception;

  Er(final Literator<T> source, final String message, final Func.Bi<? super String, ? super Throwable, ? extends E> exception) {
    this.source = source;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public final T fetch() throws Throwable {
    try {
      if (hasNext()) {
        return source.fetch();
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }

  @Override
  public final boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>nothing().as(routine);
  }
}

final class Catch<T> implements Cursor<T> {
  private final Literator<T> source;
  private final Cons.Uni<? super Throwable> catch$;

  Catch(final Literator<T> source, final Cons.Uni<? super Throwable> catch$) {
    this.source = source;
    this.catch$ = catch$;
  }

  @Override
  public final T fetch() {
    try {
      return source.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public final boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>nothing().as(routine);
  }
}
