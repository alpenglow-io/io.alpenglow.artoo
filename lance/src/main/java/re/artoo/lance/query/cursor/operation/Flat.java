package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Flat<ELEMENT>(Fetch<Fetch<ELEMENT>> fetch, Flatten<ELEMENT> flatten) implements Cursor<ELEMENT> {
  public Flat(Fetch<Fetch<ELEMENT>> fetch) {
    this(fetch, new Flatten<>());
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext = fetch.hasNext() || (flatten.fetch != null && flatten.fetch.element.hasNext());

    if (flatten.hasNext && !flatten.fetch.element.hasNext()) {
      flatten.fetch = fetch.next((i, e) -> new Value<>() {
        {
          index = i;
          element = e;
        }
      });
    }

    return flatten.hasNext;
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return null;
  }

  @Override
  public Next<ELEMENT> next() {
    return hasNext() ? flatten.fetch.element.next() : FetchException.byThrowingCantFetchNextElement("flat-map", "flattable");
  }

  private static final class Flatten<NEXT> {
    private boolean hasNext = true;
    private Value<Fetch<NEXT>> fetch;
  }
}
