package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Peek<ELEMENT>(Fetch<ELEMENT> fetch, TryIntConsumer1<? super ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement()
      ? fetch
      .element((index, element) -> {
        operation.invoke(index, element);
        return then.invoke(index, element);
      })
      : FetchException.byThrowingCantFetchNextElement("peek", "peekable");
  }
}
