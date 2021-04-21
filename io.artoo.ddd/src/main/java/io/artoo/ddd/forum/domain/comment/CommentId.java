package io.artoo.ddd.forum.domain.comment;

import io.artoo.ddd.core.Id;
import io.artoo.lance.value.Value;

public record CommentId(Id value) implements Value<Id, CommentId> {
  public CommentId() { this(Id.random()); }

  static CommentId random() { return new CommentId(); }

  @Override
  public Id tryGet() { return value; }
}
