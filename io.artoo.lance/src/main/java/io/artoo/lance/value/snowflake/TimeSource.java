package io.artoo.lance.value.snowflake;

import java.time.Instant;

public interface TimeSource {

  static TimeSource fromEpoch() {
    return new Epoch(Instant.ofEpochMilli(1577836800000L));
  }

  final class Epoch implements TimeSource {
    private final  long start;
    private final long offset;
    private final Instant epoch;

    private Epoch(final Instant epoch) {

    }
  }
}
