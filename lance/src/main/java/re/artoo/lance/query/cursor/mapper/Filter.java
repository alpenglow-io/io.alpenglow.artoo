package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT tick() throws Throwable {
    if (!isTickable()) return null;
    ELEMENT tick = probe.tick();
    return condition.invoke(0, tick) ? tick : null;
  }

  @Override
  public boolean isTickable() throws Throwable {
    return probe.isTickable();
  }
}
