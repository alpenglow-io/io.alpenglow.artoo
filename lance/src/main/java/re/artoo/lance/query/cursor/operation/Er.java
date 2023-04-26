package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.OperationException;
import re.artoo.lance.query.cursor.Fetch;

public final class Er<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback;

  public Er(Fetch<ELEMENT> fetch, TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback) {
    super("er", "erratic");
    this.fetch = fetch;
    this.fallback = fallback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    return !hasElement && (hasElement = fetch.hasElement());
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement ? fetch.element(then) : OperationException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      return fetch.thrownAt(index -> then.invoke(index, fallback.invoke(index, throwable)));
    } finally {
      hasElement = false;
    }
  }
}
