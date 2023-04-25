package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Map<ELEMENT, RETURN>(Fetch<ELEMENT> fetch, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {
  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super RETURN, ? extends NEXT> then) throws Throwable {
    return hasElement() ? fetch.element((index, element) -> then.invoke(index, operation.invoke(index, element))) : FetchException.byThrowingCantFetchNextElement("map", "mappable");
  }
}
