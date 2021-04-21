package io.artoo.ddd.forum.domain.comment;

import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

public record CommentText(String value) implements Value<String, CommentText> {
  private static final int min = 2;
  private static final int max = 4000;

  public CommentText { assert value.length() > min && value.length() <= max; }

  static One<CommentText> of(final String value) {
    return One.maybe(value)
      .where(it -> value.length() > min && value.length() <= max)
      .select(CommentText::new);
  }

  @Override
  public String tryGet() { return value; }
}
