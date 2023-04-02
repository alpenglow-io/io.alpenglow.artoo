package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.InvokeException;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Catch<ELEMENT>(Fetch<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @Override
  public Next<ELEMENT> next() {
    again:
    try {
      return hasNext() ? probe.next() : FetchException.byThrowingCantFetchNextElement("catch", "catchable");
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      break again;
    }

    throw new IllegalStateException("Can't fetch next element, unreachable state happened");
  }
}
