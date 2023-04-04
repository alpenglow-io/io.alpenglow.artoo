package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

@SuppressWarnings("UnnecessaryBreak")
public record Catch<ELEMENT>(Fetch<ELEMENT> fetch, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    again:
    try {
      return hasNext() ? fetch.next(then) : FetchException.byThrowingCantFetchNextElement("catch", "catchable");
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      break again;
    }

    return FetchException.byThrowingCantFetchNextElement("catch", "catchable");
  }
}
