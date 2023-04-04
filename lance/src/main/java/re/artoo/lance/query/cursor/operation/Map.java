package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.Fetch.Next.Indexed;

public record Map<ELEMENT, RETURN>(Fetch<ELEMENT> fetch, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {

  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public Next<RETURN> fetch() {
    return hasNext() ?
      switch (fetch.next()) {
        case Next<ELEMENT> it when it instanceof Indexed<ELEMENT>(var index, var element) -> Next.of(index, operation.apply(index, element));
        case Next<ELEMENT> it -> Next.of(operation.apply(-1, it.element()));
      }
      : FetchException.byThrowingCantFetchNextElement("map", "mappable");
  }
}
