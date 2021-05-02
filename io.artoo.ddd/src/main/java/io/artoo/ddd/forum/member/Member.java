package io.artoo.ddd.forum.member;

import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

public sealed interface Member {
  enum Namespace implements Member {}

  record FullName(String value) implements Value<String, FullName> {
    public FullName { assert value != null && value.length() > 3 && value.length() <= 200; }

    static One<FullName> of(final String value) {
      return One.maybe(value)
        .where(it -> it.length() > 3 && value.length() <= 200)
        .select(FullName::new);
    }
  }
}
