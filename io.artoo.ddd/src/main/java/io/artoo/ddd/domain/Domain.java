package io.artoo.ddd.domain;

import io.artoo.lance.query.One;
import io.artoo.lance.value.Symbol;

import java.time.Instant;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public enum Domain {
  ;

  public sealed interface Object permits Aggregate, Command, Event {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }

  public non-sealed interface Event extends Domain.Object {}
  public record EventMessage(Symbol eventId, Domain.Event event, Id aggregateId, Instant persistedAt, Instant emittedAt) {}

  public non-sealed interface Command extends Domain.Object {}

  public non-sealed interface Aggregate extends One<UnitOfWork>, Domain.Object {}

  public sealed interface UnitOfWork {
    Symbol id();
    Id aggregateId();
    Changes changes();

    static UnitOfWork work(Id id) {
      return new Work(id, Changes.none());
    }

    static UnitOfWork work(Event... events) {
      return new Work(Id.random(), Changes.uncommitted(events));
    }

    UnitOfWork change(Domain.Event... events);
  }

  private record Work(Symbol id, Id aggregateId, Changes changes) implements UnitOfWork {
    private Work(Id aggregateId, Changes changes) {
      this(Symbol.unique(), aggregateId, changes);
    }
    @Override
    public UnitOfWork change(Domain.Event... events) {
      return new Work(aggregateId, changes.append(events));
    }
  }
}
