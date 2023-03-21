package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;
import re.artoo.lance.value.Array;

public record Distinct<ELEMENT>(Probe<? extends ELEMENT> probe, Atom<Array<ELEMENT>> track, Atom<ELEMENT> atom,
                                TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Distinct(Probe<? extends ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.notFetched(Array.none()), Atom.reference(), condition);
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on reduce cursor (no more reducible steps?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    while (probe.canFetch() && atom.isFetched()) {
      ELEMENT element = probe.fetch();
      if (!condition.invoke(atom.indexThenInc(), element) || !track.element().includes(element)) {
        atom.element(element);
      }

      track.element(track.element().push(element));
    }

    return atom.isNotFetched();
  }
}
