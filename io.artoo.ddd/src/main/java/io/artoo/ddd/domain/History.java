package io.artoo.ddd.domain;

import io.artoo.lance.query.Many;

public interface History extends Many<History.Source> {
  record Source(Id aggregateId, Domain.Event event) {}
}
