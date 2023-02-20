package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Reduce<ELEMENT, FOLDED>(
  Probe<? extends ELEMENT> probe,
  Probe<? extends FOLDED> initial,
  TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation
) implements Cursor<FOLDED> {
  @Override
  public FOLDED fetch() throws Throwable {
    var reduced = initial.fetch();
    while (probe.canFetch()) {
      reduced = operation.invoke(0, reduced, probe.fetch());
    }
    return reduced;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return probe.canFetch() || (!probe.equals(initial) && initial.canFetch());
  }
}
