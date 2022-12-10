package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.projector.Flat;
import io.alpenglow.artoo.lance.query.cursor.projector.Map;

public sealed interface Projector<FROM> extends Fetcher<FROM> permits Cursor {

  default <TO> Cursor<TO> map(final TryFunction1<? super FROM, ? extends TO> map) {
    return new Map<>(this, map);
  }

  default <TO, CURSOR extends Cursor<TO>> Cursor<TO> flatMap(final TryFunction1<? super FROM, ? extends CURSOR> flatMap) {
    return new Flat<>(new Map<>(this, flatMap));
  }
}

