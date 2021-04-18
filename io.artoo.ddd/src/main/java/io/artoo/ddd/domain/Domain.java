package io.artoo.ddd.domain;

import io.artoo.lance.query.One;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public enum Domain {
  ;

  public interface Message<R extends Record> extends One<R> {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }

  public interface Event {

  }

  public interface Command {
    Changes changes();
  }

  public interface Aggregate<R extends Record> extends One<Pending<R>> {}

  public sealed interface Pending<R extends Record> {
    Id id();
    R state();
    Changes changes();

    static <R extends Record> Pending<R> state(R record) {
      return new State<>(Id.random(), record, Changes.none());
    }

    static <R extends Record> Pending<R> state(R record, Event... events) {
      return new State<>(Id.random(), record, Changes.uncommitted(events));
    }

    Pending<R> change(R state, Domain.Event... events);
  }

  private record State<R extends Record>(Id id, R state, Changes changes) implements Pending<R> {
    @Override
    public Pending<R> change(R state, Domain.Event... events) {
      return new State<>(id, state, changes.append(events));
    }
  }
}
