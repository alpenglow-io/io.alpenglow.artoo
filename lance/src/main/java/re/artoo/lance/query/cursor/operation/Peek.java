package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Peek<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, TryIntConsumer1<? super ELEMENT> peek) implements Cursor<ELEMENT> {
  public Peek(Probe<ELEMENT> probe, TryIntConsumer1<? super ELEMENT> peek) {
    this(probe, Atom.reference(), peek);
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element from peek cursor (no more peekable steps?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    var fetch = probe.fetch();
    peek.invoke(atom.indexThenInc(), fetch);
    atom.element(fetch);

    return atom.isNotFetched();
  }
}
