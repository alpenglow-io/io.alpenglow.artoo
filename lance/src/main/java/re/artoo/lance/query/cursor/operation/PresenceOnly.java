package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

@SuppressWarnings("StatementWithEmptyBody")
public record PresenceOnly<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom) implements Cursor<ELEMENT> {
  public PresenceOnly(Probe<ELEMENT> probe) {
    this(probe, Atom.reference());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;

    for (
      ;
      probe.canFetch() && atom.element() == null;
      atom.element(probe.fetch())
    );

    return atom.element() != null;
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on presence-only operation (no more present elements?)");
  }
}
