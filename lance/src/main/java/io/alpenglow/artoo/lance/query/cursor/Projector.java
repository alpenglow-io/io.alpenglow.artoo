package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.projector.Flat;
import io.alpenglow.artoo.lance.query.cursor.projector.Map;

public sealed interface Projector<T> extends Fetcher<T> permits Cursor {

  default <R> Cursor<R> map(final Closure<? super T, ? extends R> map) {
    return new Map<>(this, map);
  }

  default <R, C extends Cursor<R>> Cursor<R> flatMap(final Closure<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

