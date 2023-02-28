package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

@SuppressWarnings("StatementWithEmptyBody")
public record PresenceOnly<ELEMENT>(Probe<ELEMENT> probe, Reference<ELEMENT> reference) implements Cursor<ELEMENT> {
  public PresenceOnly(Probe<ELEMENT> probe) {
    this(probe, Reference.iterative());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (reference.isNotFetched()) return true;

    for (
      ;
      probe.canFetch() && reference.element() == null;
      reference.element(probe.fetch())
    );

    return reference.element() != null;
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? reference.element() : FetchException.byThrowing("Can't fetch next element on coalesce operation");
  }
}
