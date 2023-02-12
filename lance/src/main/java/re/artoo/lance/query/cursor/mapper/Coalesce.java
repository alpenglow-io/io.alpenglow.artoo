package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Coalesce<ELEMENT>(Probe<ELEMENT> probe, Ticked<ELEMENT> ticked) implements Cursor<ELEMENT> {
  public Coalesce(Probe<ELEMENT> probe) {
    this(probe, Ticked.empty());
  }

  @Override
  public boolean isTickable() throws Throwable {
    if (ticked.element == null && probe.isTickable()) ticked.element = probe.tick();
    while (ticked.element == null && probe.isTickable())
      ticked.element = probe.tick();
    return ticked.element != null;
  }

  @Override
  public ELEMENT tick() throws Throwable {
    try {
      return isTickable() ? ticked.element : null;
    } finally {
      ticked.element = null;
    }
  }
}
