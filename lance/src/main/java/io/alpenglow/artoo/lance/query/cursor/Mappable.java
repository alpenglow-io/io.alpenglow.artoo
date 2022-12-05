package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.map.Flat;
import io.alpenglow.artoo.lance.query.cursor.map.Map;

public sealed interface Mappable<OLD> extends Source<OLD> permits Cursor {
  default <NEW> Cursor<NEW> map(final TryFunction1<? super OLD, ? extends NEW> map) {
    return new Map<>(this, map);
  }

  default <NEW, CURSOR extends Cursor<NEW>> Cursor<NEW> flatMap(final TryFunction1<? super OLD, ? extends CURSOR> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

