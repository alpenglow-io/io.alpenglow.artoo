package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Peek<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntConsumer1<? super ELEMENT> operation;

  public Peek(Fetchable<ELEMENT> fetchable, TryIntConsumer1<? super ELEMENT> operation) {
    this.fetchable = fetchable;
    this.operation = operation;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    return fetchable.canFetch();
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    return canFetch()
      ? fetchable
      .fetch((index, element) -> {
        operation.invoke(index, element);
        return then.invoke(index, element);
      })
      : throwing(() -> Fetchable.Exception.of("peek", "peekable"));
  }
}
