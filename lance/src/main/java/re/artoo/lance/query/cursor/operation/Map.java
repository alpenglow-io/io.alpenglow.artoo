package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, Atom<RETURN> atom, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {
  public Map(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this(probe, Atom.reference(), operation);
  }
  @Override
  public RETURN fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element from cursor (no more mappable elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    atom.element(operation.invoke(atom.indexThenInc(), probe.fetch()));

    return atom.isNotFetched();
  }
}
