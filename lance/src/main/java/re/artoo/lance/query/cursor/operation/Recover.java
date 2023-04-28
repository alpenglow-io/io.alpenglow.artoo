package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Recover<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super ELEMENT, ? super Throwable, ? extends ELEMENT> fallback;

  public Recover(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super Throwable, ? extends ELEMENT> fallback) {
    super("er", "erratic");
    this.fetch = fetch;
    this.fallback = fallback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    return hasElement || (hasElement = fetch.hasElement());
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return hasElement ? fetch.element(apply) : FetchException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      return fetch.thrownAt((index, element) -> apply.invoke(index, fallback.invoke(index, element, throwable)));
    } finally {
      hasElement = false;
    }
  }
}
