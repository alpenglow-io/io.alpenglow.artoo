package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Fold;

public sealed interface Folderator<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <FOLDED> Cursor<FOLDED> fold(FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    return new Fold<>(this, initial, operation);
  }
  default <FOLDED> Cursor<FOLDED> fold(FOLDED initial, TryFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    return fold(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }
}

