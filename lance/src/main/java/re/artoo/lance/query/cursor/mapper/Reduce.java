package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Reduce<ELEMENT, FOLDED>(
  Probe<? extends ELEMENT> probe,
  Probe<? extends FOLDED> initial,
  TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation
) implements Cursor<FOLDED> {
  @Override
  public FOLDED tick() throws Throwable {
    var reduced = initial.tick();
    while (probe.isTickable()) {
      reduced = operation.invoke(0, reduced, probe.tick());
    }
    return reduced;
  }

  @Override
  public boolean isTickable() throws Throwable {
    return probe.isTickable() || (!probe.equals(initial) && initial.isTickable());
  }
}
