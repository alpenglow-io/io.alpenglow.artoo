package io.artoo.ddd.ordering;

import io.artoo.ddd.domain.Changes;
import io.artoo.ddd.domain.Domain;

import java.time.Instant;

public enum Ordering {;

  public interface Make extends Domain.Command {
    static Make command(Instant actual) {
      return new Default(actual);
    }

    final class Default implements Make {
      private final Instant actual;

      public Default(final Instant actual) {this.actual = actual;}

      @Override
      public Changes changes() {
        return Changes.append(Made.event(actual));
      }
    }
  }
  public interface Approve extends Domain.Command {}
  public interface Revise extends Domain.Command {}
  public interface Cancel extends Domain.Command {}

  public interface Made extends Domain.Event {
    static Made event(Instant actual) {
      return new Default(actual);
    }

    record Default(Instant actual) implements Made {}
  }
}
