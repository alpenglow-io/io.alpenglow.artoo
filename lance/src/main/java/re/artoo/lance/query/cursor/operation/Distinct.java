package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.Probe.Next.Indexed;
import re.artoo.lance.query.cursor.Probe.Next.Just;

import java.util.HashSet;
import java.util.Set;

public record Distinct<ELEMENT>(Probe<ELEMENT> probe, Set<ELEMENT> elements, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Distinct(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, new HashSet<>(), condition);
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
        case Indexed<ELEMENT> it when !condition.test(it.index(), it.element()) || !elements.contains(it.element()):
          elements.add(it.element());
          return it;
        case Just<ELEMENT> it when !condition.test(-1, it.element()) || !elements.contains(it.element()):
          elements.add(it.element());
          return it;
        default:
          break again;
      }
    }

    return FetchException.byThrowingCantFetchNextElement("distinct", "distinctable");
  }
}
