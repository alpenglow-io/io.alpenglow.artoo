package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Peek<ELEMENT>(Fetch<ELEMENT> fetch, TryIntConsumer1<? super ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return hasNext() ? fetch.next((index, element) -> then.apply(index, operation.selfAccept(element, index, element))) : FetchException.byThrowingCantFetchNextElement("peek", "peekable");
  }
}
