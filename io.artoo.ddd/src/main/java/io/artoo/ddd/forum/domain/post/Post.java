package io.artoo.ddd.forum.domain.post;

import io.artoo.ddd.core.Domain;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public sealed interface Post {
  enum Type implements Post {Article, Link}

  sealed interface Id extends Value<UUID, Id> { static Post.Id uuid() { return new PostId(UUID.randomUUID()); } }
  sealed interface Title extends Value<String, Title> { static One<Post.Title> of(String value) { return PostTitle.of(value); }}
  sealed interface Text extends Value<String, Text> { static One<Post.Text> of(String value) { return PostText.of(value); }}
  sealed interface Link extends Value<URL, Link> { static One<Post.Link> of(String value) { return PostLink.of(value); }}

  interface Entity {}

  interface CreateArticle extends Func.Bi<Id, CreateArticle.Command, Domain.UnitOfWork> {
    record Command(Title title, Text text, Instant createdAt) {}
  }
  interface CreateLink extends Func.Bi<Id, CreateLink.Command, Domain.UnitOfWork> {
    record Command(Title title, Link link, Instant createdAt) {}
  }

  interface ArticleCreated extends Cons.Uni<ArticleCreated.Event> {
    record Event(Title title, Text text, Instant createdAt) {}
  }
  interface LinkCreated extends Cons.Uni<LinkCreated.Event> {
    record Event(Title title, Link link, Instant createdAt) {}
  }
}

record PostId(UUID value) implements Post.Id {}
record PostTitle(String value) implements Post.Title {
  PostTitle { assert value.length() > 2 && value.length() <= 85; }

  static One<Post.Title> of(final String value) {
    return One.maybe(value)
      .where(it -> it.length() > 2 && it.length() <= 85)
      .select(PostTitle::new);
  }
}
record PostText(String value) implements Post.Text {
  PostText { assert value.length() > 2 && value.length() <= 4000; }

  static One<Post.Text> of(final String value) {
    return One.maybe(value)
      .where(it -> it.length() > 2 && it.length() <= 4000)
      .select(PostText::new);
  }
}
record PostLink(URL value) implements Post.Link {
  static One<Post.Link> of(String value) {
    return One.maybe(value)
      .select(URL::new)
      .select(PostLink::new);
  }
}
