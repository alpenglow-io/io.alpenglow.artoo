package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Fold<ELEMENT, FOLDED>(Probe<? extends ELEMENT> probe, Atom<FOLDED> atom, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) implements Cursor<FOLDED> {
  public Fold(Probe<? extends ELEMENT> probe, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    this(probe, Atom.reference(initial), operation);
  }
  @Override
  public FOLDED fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no more reducible elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;

    while (probe.canFetch()) {
      atom.element(operation.invoke(atom.indexThenInc(), atom.element(), probe.fetch()));
    }

    return true;
  }
}
