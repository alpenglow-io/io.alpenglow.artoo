package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.mapper.Flat;
import re.artoo.lance.query.cursor.mapper.Map;

public sealed interface Mappable<T> extends Inquiry<T> permits Cursor {
  default Cursor<T> filter(TryIntPredicate1<? super T> filter) {
    return map((index, it) -> filter.invoke(index, it) ? it : null);
  }
  default Cursor<T> filter(TryPredicate1<? super T> filter) {
    return map((index, it) -> filter.invoke(it) ? it : null);
  }
  default <R> Cursor<R> map(TryFunction1<? super T, ? extends R> map) {
    return map((index, it) ->  map.invoke(it));
  }
  default <R> Cursor<R> map(TryIntFunction1<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }
  default Cursor<T> peek(TryConsumer1<? super T> peek) {
    return map(it -> peek.self(it, it));
  }
  default Cursor<T> peek(TryIntConsumer1<? super T> peek) {
    return map((index, it) -> peek.self(it, index, it));
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final TryIntFunction1<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final TryFunction1<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, (index, it) -> flatMap.invoke(it)));
  }
}

