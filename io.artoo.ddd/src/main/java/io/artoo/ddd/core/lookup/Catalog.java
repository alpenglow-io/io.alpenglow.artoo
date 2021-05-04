package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Id;

public interface Catalog {
  View view(Id id);
}
