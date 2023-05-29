package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.Flat;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Peek;

import static re.artoo.lance.query.cursor.operation.Filter.presenceOnly;

public sealed interface Mappator<ELEMENT> extends Fetch<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> peek(TryConsumer1<? super ELEMENT> peek) {
    return peek((index, element) -> peek.invoke(element));
  }

  default Cursor<ELEMENT> peek(TryIntConsumer1<? super ELEMENT> peek) {
    return new Peek<>(this, peek);
  }

  default <RETURN> Cursor<RETURN> map(TryFunction1<? super ELEMENT, ? extends RETURN> map) {
    return map((index, it) -> map.invoke(it));
  }

  default <RETURN> Cursor<RETURN> map(TryIntFunction1<? super ELEMENT, ? extends RETURN> map) {
    return new Map<>(this, map);
  }

  default <RETURN, CURSOR extends Cursor<RETURN>> Cursor<RETURN> flatMap(final TryIntFunction1<? super ELEMENT, ? extends CURSOR> flatMap) {
    return new Flat<>(new Filter<>(new Map<>(this, flatMap), presenceOnly()));
  }

  default <RETURN, CURSOR extends Cursor<RETURN>> Cursor<RETURN> flatMap(final TryFunction1<? super ELEMENT, ? extends CURSOR> flatMap) {
    return flatMap((index, element) -> flatMap.invoke(element));
  }
}

