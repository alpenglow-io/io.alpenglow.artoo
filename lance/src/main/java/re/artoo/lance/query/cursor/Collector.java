package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.func.TryIntConsumer2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Collect;

public sealed interface Collector<ELEMENT> extends Fetch<ELEMENT> permits Cursor {

  default <COLLECTED> Cursor<COLLECTED> collect(COLLECTED initial, TryIntConsumer2<? super ELEMENT, ? super COLLECTED> operation) {
    return new Collect<>(this, initial, operation);
  }

  default <COLLECTED> Cursor<COLLECTED> collect(COLLECTED initial, TryConsumer2<? super ELEMENT, ? super COLLECTED> operation) {
    return collect(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }
}
