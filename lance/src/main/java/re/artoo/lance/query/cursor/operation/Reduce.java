package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Reduce<ELEMENT>(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<ELEMENT> fetch() {
    if (hasNext()) {
      ELEMENT reduced = fetch.next().element();
      again: if (hasNext()) {
        reduced = switch (fetch.next()) {
          case Next<ELEMENT> it when it instanceof Next.Indexed<ELEMENT>(var index, var element) -> operation.apply(index, reduced, element);
          case Next<ELEMENT> it -> operation.apply(-1, reduced, it.element());
        };
        break again;
      }

      return Next.of(reduced);
    }
    return FetchException.byThrowingCantFetchNextElement("reduce", "reducible");
  }
}
