package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.Fetch.Next.Indexed;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Filter<ELEMENT>(Fetch<ELEMENT> probe, Atom<ELEMENT> atom, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() {
    return (index, element) -> element == null;
  }

  public Filter(Fetch<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), condition);
  }

  @Override
  public boolean hasNext() {
    if (atom.isNotFetched()) return true;
    if (!probe.hasNext()) return false;

    var next = probe.next();
    while (!(next instanceof Indexed<ELEMENT>(var index, var element) && condition.test(index, element)) && probe.hasNext()) {
      next = probe.next();
      atom.element(next);
    }

    return atom.isNotFetched();
  }

  @Override
  public Next<ELEMENT> next() {
    return hasNext() ? atom.elementThenFetched() : Next.failure("filter", "filterable");
  }
}
