package io.artoo.ddd.core.changes;

import io.artoo.ddd.core.Changes;
import io.artoo.ddd.core.Domain;
import io.artoo.lance.literator.Cursor;

public final class Uncommitted implements Changes {
  private final Domain.Event[] events;

  public Uncommitted(final Domain.Event... events) {this.events = events;}

  @Override
  public Cursor<Domain.Event> cursor() {
    return Cursor.open(events);
  }
}
