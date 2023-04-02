package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Reduce<ELEMENT>(Fetch<? extends ELEMENT> probe, Atom<ELEMENT> atom,
                              TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  public Reduce(Fetch<? extends ELEMENT> probe, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    this(probe, Atom.reference(), operation);
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no more reducible steps?)");
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
