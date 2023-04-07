package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Reduce;

public sealed interface Reduciator<ELEMENT> extends Fetch<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> reduce(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    return new Reduce<>(this, operation);
  }
  default Cursor<ELEMENT> reduce(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduce((index, reduced, element) -> reduce.invoke(reduced, element));
  }
}

