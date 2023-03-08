package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Reduce<ELEMENT>(Probe<? extends ELEMENT> probe, Atom<ELEMENT> atom, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  public Reduce(Probe<? extends ELEMENT> probe, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    this(probe, Atom.lazy(() -> probe.canFetch() ? probe.fetch() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no initial reducible element?)")), operation);
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no more reducible elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    while (probe.canFetch()) {
      ELEMENT element = atom.element();
      if (probe.canFetch())
        atom.element(operation.invoke(atom.indexThenInc(), element, probe.fetch()));
      else
        atom.element(element);
    }
    return true;
  }
}
