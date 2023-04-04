package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Peek<ELEMENT>(Fetch<ELEMENT> fetch, TryIntConsumer1<? super ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    return hasNext() ?
      switch (fetch.next()) {
        case Next<ELEMENT> it when it instanceof Next.Indexed<ELEMENT>(var index, var element) -> operation.selfAccept(it, index, element);
        case Next<ELEMENT> it -> operation.selfAccept(it, -1, it.element());
      }
      : FetchException.byThrowingCantFetchNextElement("peek", "peekable");
  }
}
