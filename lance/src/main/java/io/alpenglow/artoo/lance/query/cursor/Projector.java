package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.projector.Flat;
import io.alpenglow.artoo.lance.query.cursor.projector.Map;

public sealed interface Projector<SOURCE> extends Fetcher<SOURCE> permits Cursor {

  default <TARGET> Cursor<TARGET> map(final Closure<SOURCE, TARGET> map) {
    return new Map<>(this, map);
  }

  default <TARGET, CURSOR extends Cursor<TARGET>> Cursor<TARGET> flatMap(final Closure<SOURCE, ? extends CURSOR> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

