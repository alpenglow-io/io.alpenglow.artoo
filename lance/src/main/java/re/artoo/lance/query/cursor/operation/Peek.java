package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Peek<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntConsumer1<? super ELEMENT> operation;

  public Peek(Fetch<ELEMENT> fetch, TryIntConsumer1<? super ELEMENT> operation) {
    this.fetch = fetch;
    this.operation = operation;
  }

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
      : raise(() -> FetchException.of("peek", "peekable"));
  }
}
