package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.OperationException;
import re.artoo.lance.query.cursor.Fetch;

public final class Er<ELEMENT, EXCEPTION extends RuntimeException> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, EXCEPTION> fallback;

  public Er(Fetch<ELEMENT> fetch, String message, TryFunction2<? super String, ? super Throwable, EXCEPTION> fallback) {
    super("er", "erratic");
    this.fetch = fetch;
    this.message = message;
    this.fallback = fallback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      hasElement = fetch.hasElement();
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    try {
      return hasElement ? fetch.element(then) : OperationException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }
}
