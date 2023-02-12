package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Catch<ELEMENT>(Probe<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT tick() {
    try {
      return probe.tick();
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean isTickable() {
    try {
      return probe.isTickable();
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      return false;
    }
  }
}
