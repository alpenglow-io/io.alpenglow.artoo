package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Fold;

public sealed interface Reducible<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <FOLDED> Cursor<FOLDED> reduce(FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    return new Fold<>(this, initial, operation).coalesce();
  }
  default <REDUCED> Cursor<REDUCED> reduce(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return reduce(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }
  default Cursor<ELEMENT> reduce(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    return new Fold<>(this, this.next(), operation).coalesce();
  }
  default Cursor<ELEMENT> reduce(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduce((index, reduced, element) -> reduce.invoke(reduced, element));
  }
}

