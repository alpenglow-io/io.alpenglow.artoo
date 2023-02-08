package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.scope.Late;
import re.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Complementable<ELEMENT> extends Head<ELEMENT>, Tail<ELEMENT> {
  default <C extends Cursor<ELEMENT>> Cursor<ELEMENT> or(final TrySupplier1<? extends C> alternative) {
    return new Or<>(this, Let.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<ELEMENT> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

abstract non-sealed class As<T> implements Appendable<T> {
  protected final Head<T> head;

  protected As(final Head<T> head) {this.head = head;}

}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Head<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Head<T> head, final Let<? extends C> other) {
    super(head);
    this.other = other;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) {
    return hasNext() ? reference.let(it -> it.scroll(fetch)) : null;
  }
  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> head.hasNext() ? head : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends E> exception;

  Er(final Head<T> head, final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    super(head);
    this.message = message;
    this.exception = exception;
  }
  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    try {
      if (hasNext()) {
        return head.scroll(fetch);
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }
  @Override
  public boolean hasNext() {
    return head.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final TryConsumer1<? super Throwable> catch$;

  Catch(Head<T> head, TryConsumer1<? super Throwable> catch$) {
    super(head);
    this.catch$ = catch$;
  }
  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) {
    try {
      return head.scroll(fetch);
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return head.hasNext();
  }
}
