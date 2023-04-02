package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Next.Success;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() { return (index, element) -> element != null; }
  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() { return (index, element) -> element == null; }
  public Filter(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), condition);
  }
  @Override
  public boolean hasNext() {
    try {
      if (atom.isNotFetched()) return true;
      if (!probe.hasNext()) return false;

      var next = probe.next();
      while (!(next instanceof Success<ELEMENT>(var index, var element) && condition.invoke(index, element)) && probe.hasNext()) {
        next = probe.next();
        atom.element(next);
      }
    } catch (Throwable throwable) {
      atom.element(Next.failure(throwable));
    }

    return atom.isNotFetched();
  }

  @Override
  public Next<ELEMENT> next() {
    return hasNext() ? atom.elementThenFetched() : Next.failure("filter", "filterable");
  }
}
