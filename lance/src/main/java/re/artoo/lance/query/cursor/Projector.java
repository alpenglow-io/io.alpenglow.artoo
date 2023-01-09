package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Closure;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.IntClosure;
import re.artoo.lance.query.cursor.projector.Flat;
import re.artoo.lance.query.cursor.projector.Map;

public sealed interface Projector<T> extends Fetcher<T> permits Cursor {
  default <R> Cursor<R> map(final Closure<? super T, ? extends R> map) {
    return new Map<>(this, (index, it) -> map.invoke(it));
  }
  default <R> Cursor<R> map(final IntClosure<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final IntClosure<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Closure<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, (index, it) -> flatMap.invoke(it)));
  }
}

