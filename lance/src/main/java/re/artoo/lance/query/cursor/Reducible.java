package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Fold;

public sealed interface Reducible<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> reduce(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    return new Fold<>(this, this.next(), operation);
  }
  default Cursor<ELEMENT> reduce(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduce((index, reduced, element) -> reduce.invoke(reduced, element));
  }
}

