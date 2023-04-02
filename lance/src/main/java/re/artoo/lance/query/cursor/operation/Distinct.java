package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.Fetch.Next.Indexed;
import re.artoo.lance.query.cursor.operation.atom.Atom;

import java.util.HashSet;
import java.util.Set;

public record Distinct<ELEMENT>(Fetch<ELEMENT> probe, Atom<ELEMENT> atom, Set<ELEMENT> elements, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Distinct(Fetch<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), new HashSet<>(), condition);
  }

  @Override
  public boolean hasNext() {
    if (atom.isNotFetched()) return true;
    if (!probe.hasNext()) return false;

    while (probe.hasNext() && atom.isFetched()) {
      var next = probe.next();
      if (next instanceof Indexed<ELEMENT>(var index, var element) && (!condition.test(index, element) || !elements.contains(element))) {
        atom.element(next);
        elements.add(element);
      }
    }

    return atom.isNotFetched();
  }

  @Override
  public Next<ELEMENT> next() {
    return hasNext() ? atom.elementThenFetched() : FetchException.byThrowingCantFetchNextElement("distinct", "distinctable");
  }
}
