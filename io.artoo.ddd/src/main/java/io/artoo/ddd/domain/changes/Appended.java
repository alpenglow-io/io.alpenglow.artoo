package io.artoo.ddd.domain.changes;

import io.artoo.ddd.domain.Changes;
import io.artoo.ddd.domain.Domain;
import io.artoo.lance.literator.Cursor;

public final class Appended implements Changes {
  private final Changes changes;
  private final Domain.Event[] events;

  public Appended(final Changes changes, final Domain.Event... events) {
    this.changes = changes;
    this.events = events;
  }

  @Override
  public Cursor<Domain.Event> cursor() {
    return changes.concat(events).cursor();
  }
}
