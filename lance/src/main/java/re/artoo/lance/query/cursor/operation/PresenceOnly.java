package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record PresenceOnly<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom) implements Cursor<ELEMENT> {
  public PresenceOnly(Probe<ELEMENT> probe) {
    this(probe, Atom.reference());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    while (probe.canFetch() && (atom.isFetched() || atom.element() == null)) {
      atom.element(probe.fetch());
    }
    if (atom.element() == null) atom.unfetch();

    return atom.element() != null;
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on presence-only condition (no more present steps?)");
  }
}
