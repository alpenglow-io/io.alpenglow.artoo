package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Filter;

import java.util.Objects;

public sealed interface Filterable<ELEMENT> extends Fetchable<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> filter(TryIntPredicate1<? super ELEMENT> filter) {
    return new Filter<>(this, filter);
  }

  default Cursor<ELEMENT> filter(TryPredicate1<? super ELEMENT> filter) {
    return filter((index, element) -> filter.invoke(element));
  }

  default Cursor<ELEMENT> evaluable() {
    return filter(Objects::nonNull);
  }

  default Cursor<ELEMENT> nullable() {
    return filter(Objects::isNull);
  }
}
