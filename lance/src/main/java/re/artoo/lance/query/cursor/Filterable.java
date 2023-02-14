package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.mapper.Coalesce;
import re.artoo.lance.query.cursor.mapper.Filter;

public sealed interface Filterable<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> filter(TryIntPredicate1<? super ELEMENT> filter) {
    return new Filter<>(this, filter).coalesce();
  }
  default Cursor<ELEMENT> filter(TryPredicate1<? super ELEMENT> filter) {
    return filter((index, element) -> filter.invoke(element));
  }
  default Cursor<ELEMENT> coalesce() {
    return new Coalesce<>(this);
  }
}
