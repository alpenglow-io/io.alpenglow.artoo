package io.artoo.lance.literator.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.scope.Late;
import io.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Substitutable<T> extends Literator<T> {
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

abstract class As<T> implements Transformable<T> {
  protected final Literator<T> source;

  protected As(final Literator<T> source) {this.source = source;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>nothing().as(routine);
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Literator<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Literator<T> source, final Let<? extends C> other) {
    super(source);
    this.other = other;
  }

  @Override
  public T fetch() {
    return hasNext() ? reference.let(Literator::fetch) : null;
  }

  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> source.hasNext() ? source : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final Func.Bi<? super String, ? super Throwable, ? extends E> exception;

  Er(final Literator<T> source, final String message, final Func.Bi<? super String, ? super Throwable, ? extends E> exception) {
    super(source);
    this.message = message;
    this.exception = exception;
  }

  @Override
  public T fetch() {
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
  public boolean hasNext() {
    return source.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final Cons.Uni<? super Throwable> catch$;

  Catch(final Literator<T> source, final Cons.Uni<? super Throwable> catch$) {
    super(source);
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    try {
      return source.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }
}
