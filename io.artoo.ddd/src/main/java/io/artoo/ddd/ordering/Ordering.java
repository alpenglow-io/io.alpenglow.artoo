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
        return Changes.uncommitted(Made.event(actual));
      }
    }
  }
  public interface Approve extends Domain.Command {}
  public interface Revise extends Domain.Command {}
  public interface Cancel extends Domain.Command {}

  public interface Made extends Domain.Event {
    static Made event() {
      return new Default();
    }

    record Default() implements Made {}
  }

  public interface Approved extends Domain.Event {
    static Approved event(Instant actual) {
      return new Default(actual);
    }

    record Default(Instant actual) implements Approved {}
  }

  public interface Revised extends Domain.Event {
    static Revised event(Instant actual) {
      return new Default(actual);
    }

    record Default(Instant actual) implements Revised {}
  }

  public interface ReviseConfirmed extends Domain.Event {
    static ReviseConfirmed event(Instant actual) {
      return new Default(actual);
    }

    record Default(Instant actual) implements ReviseConfirmed {}
  }

  public interface ReviseCancelled extends Domain.Event {
    static ReviseCancelled event(Instant actual) {
      return new Default(actual);
    }

    record Default(Instant actual) implements ReviseCancelled {}
  }
}
