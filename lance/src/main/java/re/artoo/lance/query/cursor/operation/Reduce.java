package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Reduce<ELEMENT>(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    if (hasElement()) {
      class Reduced { ELEMENT value = fetch.next(); }
      final var reduced = new Reduced();
      while (hasElement()) {
        reduced.value = fetch.element((index, element) -> operation.invoke(index, reduced.value, element));
      }

      return then.invoke(-1, reduced.value);
    }
    return FetchException.byThrowingCantFetchNextElement("reduce", "reducible");
  }
}
