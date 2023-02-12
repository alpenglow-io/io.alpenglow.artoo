package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Probe<ELEMENT> probe, String message, TryFunction2<? super String, ? super Throwable, ? extends EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT tick() {
    try {
      if (isTickable()) {
        return probe.tick();
      } else {
        throw fallback.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw fallback.apply(message, throwable);
    }
  }

  @Override
  public boolean isTickable() throws Throwable {
    return probe.isTickable();
  }
}
