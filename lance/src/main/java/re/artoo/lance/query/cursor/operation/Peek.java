package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Peek<ELEMENT>(Probe<ELEMENT> probe, TryIntConsumer1<? super ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    return hasNext() ?
      switch (probe.fetch()) {
        case Next<ELEMENT> it when it instanceof Next.Indexed<ELEMENT>(var index, var element) -> operation.selfAccept(it, index, element);
        case Next<ELEMENT> it -> operation.selfAccept(it, -1, it.element());
      }
      : FetchException.byThrowingCantFetchNextElement("peek", "peekable");
  }
}
