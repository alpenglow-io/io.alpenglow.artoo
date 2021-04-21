package io.artoo.ddd.core;

import io.artoo.ddd.core.changes.Appended;
import io.artoo.ddd.core.changes.Uncommitted;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public sealed interface Changes extends Many<Domain.Event> permits Appended, Uncommitted {
  static Changes none() {
    return Cursor::nothing;
  }

  static Changes uncommitted(Domain.Event... events) {
    return new Uncommitted(events);
  }

  default Changes append(Domain.Event... events) {
    return new Appended(this, events);
  }
}
