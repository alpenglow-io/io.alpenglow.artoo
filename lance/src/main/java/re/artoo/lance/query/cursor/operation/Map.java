package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.Probe.Next.Indexed;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {

  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @Override
  public Next<RETURN> fetch() {
    return hasNext() ?
      switch (probe.fetch()) {
        case Next<ELEMENT> it when it instanceof Indexed<ELEMENT>(var index, var element) -> Next.of(index, operation.apply(index, element));
        case Next<ELEMENT> it -> Next.of(operation.apply(-1, it.element()));
      }
      : FetchException.byThrowingCantFetchNextElement("map", "mappable");
  }
}
