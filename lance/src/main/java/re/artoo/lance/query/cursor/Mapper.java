package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.IntClosure;
import re.artoo.lance.query.cursor.mapper.Flat;
import re.artoo.lance.query.cursor.mapper.Map;

public sealed interface Mapper<T> extends Fetcher<T> permits Cursor {
  default Cursor<T> filter(TryIntPredicate1<? super T> filter) {
    return new Map<>(this, (index, it) -> filter.invoke(index, it) ? it : null);
  }
  default Cursor<T> filter(TryPredicate1<? super T> filter) {
    return new Map<>(this, (index, it) -> filter.invoke(it) ? it : null);
  }
  default <R> Cursor<R> map(TryFunction1<? super T, ? extends R> map) {
    return new Map<>(this, (index, it) -> map.invoke(it));
  }
  default <R> Cursor<R> map(TryIntFunction1<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final IntClosure<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Closure<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, (index, it) -> flatMap.invoke(it)));
  }
}

