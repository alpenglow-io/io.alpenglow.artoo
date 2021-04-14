package io.artoo.ddd.domain;

import io.artoo.ddd.domain.Domain.Event.Log;
import io.artoo.ddd.domain.event.Id;
import io.artoo.ddd.domain.util.Array;
import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

import java.time.Instant;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public enum Domain {
  ;

  public sealed interface Message permits Event, Command {
    default String $name() {
      return simpleNameOf(this).to(Case.Kebab);
    }
  }

  public non-sealed interface Event extends Message {
    record Log(Id eventId, Event event, Id aggregateId, String aggregateName, Instant createdAt) {
    }
  }

  public non-sealed interface Command extends Message {

  }


  enum Events implements Many<Log>, Array {
    Logs;
    private Log[] logs = new Log[0];

    @Override
    public Cursor<Log> cursor() {
      return Cursor.open(copy(logs));
    }
  }

  public static void main(String[] args) {
    record CreateMany(Id... ids) implements Command {}
    record Create(Id id) implements Command {}
    record Created(Id id) implements Event {}
    final var logs = Events.Logs;

    One.from(new CreateMany(Id.random(), Id.random()))
      .select(CreateMany::ids)
      .selection(Many::from)
      .select(Create::new)
      .select(Create::id)
      .select(Created::new)
      .select(event -> new Log(Id.random(), event, Id.random(), "aggregate", Instant.now()))
      .selection((Func.Uni<Log, Many<Log>>) logs::concat)
      .eventually();

  }
}
