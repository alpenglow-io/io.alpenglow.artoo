package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Atom<Probe<ELEMENT>> mapped, Atom<ELEMENT> flatten) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, Atom.reference(), Atom.reference());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (flatten.isNotFetched()) return true;

    while ((mapped.element() == null || !mapped.element().canFetch()) && probe.canFetch()) {
      mapped.element(probe.fetch());
    }

    if ((mapped.element() != null && mapped.element().canFetch())) flatten.element(mapped.element().fetch());

    return flatten.isNotFetched() || (mapped.element() != null && mapped.element().canFetch());
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? flatten.elementThenFetched() : FetchException.byThrowing("Can't fetch next element from cursor (no more flattable elements?)");
  }

}
