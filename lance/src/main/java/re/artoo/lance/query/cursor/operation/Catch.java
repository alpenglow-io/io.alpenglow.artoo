package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Catch<ELEMENT>(Probe<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    again:
    try {
      return hasNext() ? probe.fetch() : FetchException.byThrowingCantFetchNextElement("catch", "catchable");
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      break again;
    }

    throw new IllegalStateException("Can't fetch next element, unreachable state happened");
  }
}
