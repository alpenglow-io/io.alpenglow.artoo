package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Fetch<ELEMENT> probe, String message, TryFunction2<? super String, ? super Throwable, EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @Override
  public Next<ELEMENT> next() {
    try {
      return hasNext() ? probe.next() : FetchException.byThrowingCantFetchNextElement("er", "erratic");
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }
}
