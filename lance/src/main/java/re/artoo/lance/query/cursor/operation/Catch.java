package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Catch<ELEMENT>(Probe<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT fetch() {
    try {
      return probe.fetch();
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean canFetch() {
    try {
      return probe.canFetch();
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      return false;
    }
  }
}
