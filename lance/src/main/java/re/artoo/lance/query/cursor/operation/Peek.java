package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Peek<ELEMENT> extends Current<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntConsumer1<? super ELEMENT> operation;

  public Peek(Fetch<ELEMENT> fetch, TryIntConsumer1<? super ELEMENT> operation) {
    super(fetch, "peek", "peekable");
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) fetch.element(this::set);
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> func) throws Throwable {
    return super.element(func.invokeAfterwards((index, element) -> {
      operation.invoke(index, element);
      return element;
    }));
  }
}
