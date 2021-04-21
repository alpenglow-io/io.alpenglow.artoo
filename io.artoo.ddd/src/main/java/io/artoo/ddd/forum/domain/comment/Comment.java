package io.artoo.ddd.forum.domain.comment;

import io.artoo.ddd.core.Domain;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

import java.util.UUID;

public interface Comment extends Domain.Aggregate {
  record Id(UUID value) implements Value<UUID, Id> { static Id uuid() { return new Id(UUID.randomUUID()); } }

  record Text(String value) implements Value<String, Text> {
    public Text { assert value.length() > 2 && value.length() <= 4000; }

    static One<Text> of(String value) {
      return One.maybe(value).where(it -> value.length() > 2 && value.length() <= 4000).select(Text::new);
    }
  }

  static Comment create(Comment.Id id) { return new Root(id); }

  default Comment post()
}

final class Root implements Comment {
  private final Comment.Id id;

  Root(Id id) {
    this.id = id;
  }

  @Override
  public Cursor<Domain.UnitOfWork> cursor() {
    return null;
  }
}
