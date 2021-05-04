package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.query.Many;

public interface View extends Many<View.Preview> {
  record Preview(Id correlationId, Domain.Tract... tracts) {}


}
