package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Open<ELEMENT>(ELEMENT[] elements, Atom<ELEMENT> atom) implements Cursor<ELEMENT> {
  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, Atom.reference());
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element in open cursor (no more elements?)");
  }

  @Override
  public Probe<ELEMENT> rewind() {
    return new Open<>(elements);
  }

  @Override
  public boolean canFetch() {
    if (atom.isNotFetched()) return true;
    if (atom.index() >= elements.length) return false;

    atom.element(elements[atom.indexThenInc()]);

    return true;
  }
}
