package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT fetch() throws Throwable {
    if (!canFetch()) return null;
    ELEMENT tick = probe.fetch();
    return condition.invoke(0, tick) ? tick : null;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return probe.canFetch();
  }
}
