package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Map<ELEMENT, RETURN>(Fetch<ELEMENT> fetch, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {

  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super RETURN, ? extends NEXT> then) {
    return hasNext() ? fetch.next((index, element) -> then.apply(index, operation.apply(index, element))) : FetchException.byThrowingCantFetchNextElement("map", "mappable");
  }
}
