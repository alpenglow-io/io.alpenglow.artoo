package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;

public sealed interface Collector<ELEMENT> extends Fetch<ELEMENT> permits Cursor {

  default <RETURN> RETURN collect(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return returns.invoke(0, this.next());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default <RETURN> RETURN collect(TryFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return returns.invoke(this.next());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default ELEMENT collect() {
    return collect((index, it) -> it);
  }
}
