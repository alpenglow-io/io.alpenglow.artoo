package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.artoo.lance.query.Many;

public interface Pending<P extends Pending<P, C>, C extends Domain.Change> extends Many<C> {
  P flush();
}
