package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;
import re.artoo.lance.scope.Late;
import re.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Replacer<T> extends Inquiry<T> {
  default <C extends Cursor<T>> Cursor<T> or(final TrySupplier1<? extends C> alternative) {
    return new Or<>(this, Let.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

abstract non-sealed class As<T> implements Convertor<T> {
  protected final Inquiry<T> inquiry;

  protected As(final Inquiry<T> inquiry) {this.inquiry = inquiry;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return switch (inquiry) {
      case Cursor<T> cursor -> cursor.as(routine);
      default -> Cursor.<T>empty().as(routine);
    };
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Inquiry<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Inquiry<T> inquiry, final Let<? extends C> other) {
    super(inquiry);
    this.other = other;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) {
    return hasNext() ? reference.let(it -> it.traverse(fetch)) : null;
  }
  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> inquiry.hasNext() ? inquiry : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends E> exception;

  Er(final Inquiry<T> inquiry, final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    super(inquiry);
    this.message = message;
    this.exception = exception;
  }
  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    try {
      if (hasNext()) {
        return inquiry.traverse(fetch);
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }
  @Override
  public boolean hasNext() {
    return inquiry.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final TryConsumer1<? super Throwable> catch$;

  Catch(Inquiry<T> inquiry, TryConsumer1<? super Throwable> catch$) {
    super(inquiry);
    this.catch$ = catch$;
  }
  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) {
    try {
      return inquiry.traverse(fetch);
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return inquiry.hasNext();
  }
}
