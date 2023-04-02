package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Distinct;

public sealed interface Collector<ELEMENT> extends Fetch<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> distinct(TryIntPredicate1<? super ELEMENT> condition) {
    return new Distinct<>(this, condition);
  }

  default <RETURN> RETURN collect(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return returns.invoke(0, fetch());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default <RETURN> RETURN collect(TryFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return returns.invoke(fetch());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default ELEMENT collect() {
    return collect((index, it) -> it);
  }
}
