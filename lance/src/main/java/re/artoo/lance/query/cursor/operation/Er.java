package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Fetch<ELEMENT> fetch, String message, TryFunction2<? super String, ? super Throwable, EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    try {
      return hasNext() ? fetch.next(then) : FetchException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }
}
