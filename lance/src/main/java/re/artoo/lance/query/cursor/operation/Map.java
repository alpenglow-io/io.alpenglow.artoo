package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.OperationException;
import re.artoo.lance.query.cursor.Fetch;

public final class Map<ELEMENT, RETURN> extends Head<RETURN> implements Cursor<RETURN> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction1<? super ELEMENT, ? extends RETURN> operation;

  public Map(Fetch<ELEMENT> fetch, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    super("map", "mappable");
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) hasElement = fetch.hasElement();
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super RETURN, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement ? fetch.element((index, element) -> then.invoke(index, operation.invoke(index, element))) : OperationException.byThrowingCantFetchNextElement("map", "mappable");
    } finally {
      hasElement = false;
    }
  }
}
