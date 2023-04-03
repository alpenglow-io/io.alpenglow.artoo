package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.Probe.Next.Indexed;
import re.artoo.lance.query.cursor.Probe.Next.Just;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() {
    return (index, element) -> element == null;
  }

  public Filter(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), condition);
  }

  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<ELEMENT> fetch() {
    again: if (hasNext()) {
      switch (probe.fetch()) {
        case Indexed<ELEMENT> it when condition.test(it.index(), it.element()):
          return it;
        case Just<ELEMENT> it when condition.test(-1, it.element()):
          return it;
        default:
          break again;
      }
    }

    return FetchException.byThrowingCantFetchNextElement("filter", "filterable");
  }
}
