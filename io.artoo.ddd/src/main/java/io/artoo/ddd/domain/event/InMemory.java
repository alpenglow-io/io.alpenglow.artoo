package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.EventStore;
import io.artoo.lance.literator.Cursor;

import java.util.ArrayList;
import java.util.List;

import static io.artoo.ddd.domain.Domain.Aggregate;
import static io.artoo.ddd.domain.Domain.Pending;

public enum InMemory implements EventStore {
  Store;

  private final List<Log> logs = new ArrayList<>();

  @Override
  public Cursor<Log> cursor() {
    return Cursor.iteration(logs.iterator());
  }

  @Override
  public <A extends Aggregate<R>, R extends Record> A commit(A aggregate) {
    return aggregate
      .selection(Pending::changes)
      .select(event -> event.hashCode())
      .;
  }
}
