package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

@SuppressWarnings("UnnecessaryBreak")
public record Reduce<ELEMENT>(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    if (hasNext()) {
      class Reduced { ELEMENT value = fetch.next(); }
      final var reduced = new Reduced();
      again: if (hasNext()) {
        reduced.value = fetch.next((index, element) -> operation.apply(index, reduced.value, element));
        break again;
      }

      return then.apply(-1, reduced.value);
    }
    return FetchException.byThrowingCantFetchNextElement("reduce", "reducible");
  }
}
