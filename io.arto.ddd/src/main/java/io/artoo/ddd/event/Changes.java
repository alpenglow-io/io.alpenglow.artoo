package io.artoo.ddd.event;

import io.artoo.ddd.Domain;
import io.artoo.lance.query.Many;

public interface Changes<C extends Domain.Change, F extends Changes<C, F>> extends Many<C> {
  F flush();
}
