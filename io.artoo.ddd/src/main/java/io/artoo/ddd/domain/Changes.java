package io.artoo.ddd.domain;

import io.artoo.ddd.domain.changes.Appended;
import io.artoo.ddd.domain.changes.Initial;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public sealed interface Changes extends Many<Domain.Event> permits Appended, Initial {
  static Changes none() {
    return Cursor::nothing;
  }

  static Changes append(Domain.Event... events) {
    return new Initial(events);
  }

  static Changes append(Changes changes, Domain.Event... events) {
    return new Appended(changes, events);
  }
}
