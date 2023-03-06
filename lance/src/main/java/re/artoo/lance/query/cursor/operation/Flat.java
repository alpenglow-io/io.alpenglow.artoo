package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Atom<Probe<ELEMENT>> atom, Atom<ELEMENT> flatAtom) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, Atom.reference(), Atom.reference());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (flatAtom.isNotFetched()) return true;
    var source = atom.element();
    if (source != null && !source.canFetch() && !probe.canFetch()) return false;

    if ((source == null && probe.canFetch()) || (source != null && !source.canFetch() && probe.canFetch())) {
      source = probe.fetch();
      atom.element(source);
    }

    if (source != null && source.canFetch()) {
      flatAtom.element(source.fetch());
    }

    return true;
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? flatAtom.element() : FetchException.byThrowing("Can't fetch next element from cursor (no more flattable elements?)");
  }

}
