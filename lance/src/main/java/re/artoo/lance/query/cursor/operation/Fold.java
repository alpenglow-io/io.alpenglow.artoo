package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Fold<ELEMENT, FOLDED>(Fetch<ELEMENT> fetch, Value<FOLDED> folded, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) implements Cursor<FOLDED> {
  public Fold(Fetch<ELEMENT> fetch, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    this(fetch, new Value<>() {{ element = initial; fetched = false; }}, operation);
  }
  @Override
  public boolean hasElement() throws Throwable {
    while (fetch.hasElement()) {
      folded.set(fetch.element((index, element) -> operation.invoke(index, folded.element, element)));
    }
    return !folded.fetched;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super FOLDED, ? extends NEXT> then) throws Throwable {
    return hasElement() ? folded.get(then) : FetchException.byThrowingCantFetchNextElement("fold", "foldable");
  }
}
