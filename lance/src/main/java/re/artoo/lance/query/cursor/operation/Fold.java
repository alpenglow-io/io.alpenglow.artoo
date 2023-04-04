package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.Fetch.Next.Indexed;

public record Fold<ELEMENT, FOLDED>(Fetch<ELEMENT> fetch, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) implements Cursor<FOLDED> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<FOLDED> fetch() {
    FOLDED folded = initial;
    again: if (hasNext()) {
      folded = switch (fetch.next()) {
        case Next<ELEMENT> it when it instanceof Indexed<ELEMENT>(var index, var element) -> operation.apply(index, folded, element);
        case Next<ELEMENT> it -> operation.apply(-1, folded, it.element());
      };
      break again;
    }
    return Next.of(folded);
  }
}
