package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Probe<ELEMENT> probe, String message, TryFunction2<? super String, ? super Throwable, ? extends EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT fetch() {
    try {
      if (canFetch()) {
        return probe.fetch();
      } else {
        throw fallback.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }

  @Override
  public boolean canFetch() throws Throwable {
    return probe.canFetch();
  }
}
