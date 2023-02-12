package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Append<ELEMENT>(Probe<? extends ELEMENT> head, Probe<? extends ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT tick() throws Throwable {
    return head.isTickable()
      ? head.tick()
      : tail.isTickable()
      ? tail.tick()
      : null;
  }

  @Override
  public boolean isTickable() throws Throwable {
    return head.isTickable() || tail.isTickable();
  }
}
