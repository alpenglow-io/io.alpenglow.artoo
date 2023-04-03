package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Reduce<ELEMENT>(Probe<ELEMENT> probe, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<ELEMENT> fetch() {
    if (hasNext()) {
      ELEMENT reduced = probe.fetch().element();
      again: if (hasNext()) {
        reduced = switch (probe.fetch()) {
          case Next<ELEMENT> it when it instanceof Next.Indexed<ELEMENT>(var index, var element) -> operation.apply(index, reduced, element);
          case Next<ELEMENT> it -> operation.apply(-1, reduced, it.element());
        };
        break again;
      }

      return Next.of(reduced);
    }
    return FetchException.byThrowingCantFetchNextElement("reduce", "reducible");
  }
}
