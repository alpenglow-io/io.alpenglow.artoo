package io.artoo.ddd.domain;

import io.artoo.ddd.domain.event.Id;
import io.artoo.lance.query.One;

import java.time.Instant;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public enum Domain {;
  public interface Event {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }

    record Log(Id eventId, String eventName, Domain.Event event, Id aggregateId, String aggregateName, Instant createdAt) implements Event {}
  }
  public interface Command {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }
}
