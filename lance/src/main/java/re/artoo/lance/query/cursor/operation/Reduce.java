package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Reduce<ELEMENT>(Probe<? extends ELEMENT> probe, Atom<ELEMENT> atom,
                              TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  public Reduce(Probe<? extends ELEMENT> probe, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    this(probe, Atom.reference(), operation);
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no more reducible elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    var fetched = probe.fetch();
    do {
      if (atom.isFetched()) atom.element(fetched);
      if (probe.canFetch()) {
        atom.element(
          operation.invoke(atom.indexThenInc(), atom.element(), probe.fetch())
        );
      }
    } while (probe.canFetch());

    return atom.element() != null && atom.isNotFetched();
  }
}
