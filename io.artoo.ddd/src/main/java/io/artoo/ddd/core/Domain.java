package io.artoo.ddd.core;

import io.artoo.lance.query.One;
import io.artoo.lance.tuple.Triple;
import io.artoo.lance.value.Symbol;

import java.time.Instant;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public sealed interface Domain {
  enum Type implements Domain { Write, Read}

  sealed interface Message permits Act, Fact {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }

  non-sealed interface Fact extends Message {}
  non-sealed interface Act extends Message {
    Id modelId();
  }


  public sealed interface UnitOfWork extends Triple<Symbol, Id, Changes> {
    Symbol id();
    Id aggregateId();
    Changes changes();

    @Override default Symbol first() { return id(); }
    @Override default Id second() { return aggregateId(); }
    @Override default Changes third() { return changes(); }

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
