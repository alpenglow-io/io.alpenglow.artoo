package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public record Fold<ELEMENT, FOLDED>(Fetch<ELEMENT> fetch, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) implements Cursor<FOLDED> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super FOLDED, ? extends NEXT> then) {
    class Folded { FOLDED value = initial; }
    final var folded = new Folded();
    again:
    if (hasNext()) {
      folded.value = fetch.next((index, element) -> operation.apply(index, folded.value, element));
    }
    return then.apply(-1, folded.value);
  }
}
