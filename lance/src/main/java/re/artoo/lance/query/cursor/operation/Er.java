package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Fetch<ELEMENT> fetch, String message, TryFunction2<? super String, ? super Throwable, EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    try {
      return hasElement() ? fetch.element(then) : FetchException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }
}
