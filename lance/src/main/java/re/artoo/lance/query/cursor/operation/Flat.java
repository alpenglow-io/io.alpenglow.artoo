package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Flat<ELEMENT>(Fetch<Fetch<ELEMENT>> probe, Atom<Fetch<ELEMENT>> mapped, Atom<ELEMENT> flatten) implements Cursor<ELEMENT> {
  public Flat(Fetch<Fetch<ELEMENT>> probe) {
    this(probe, Atom.reference(), Atom.reference());
  }

  @Override
  public boolean hasNext() {
    if (flatten.isNotFetched()) return true;

    while ((mapped.element() == null || !(mapped.element() instanceof Next.Success<Fetch<ELEMENT>>(var index, var element) && element.hasNext()) && probe.hasNext())) {
      mapped.element(probe.next());
    }

    if (mapped.element() instanceof Success<Fetch<ELEMENT>>(var index, var elementProbe) && elementProbe.hasNext()) flatten.element(elementProbe.next());

    return flatten.isNotFetched() || (mapped.element() != null && mapped.element().canFetch());
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? flatten.elementThenFetched() : FetchException.byThrowing("Can't fetch next element from cursor (no more flattable steps?)");
  }

}
