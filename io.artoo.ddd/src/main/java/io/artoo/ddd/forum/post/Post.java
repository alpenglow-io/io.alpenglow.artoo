package io.artoo.ddd.forum.post;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public sealed interface Post {
  enum Type implements Post {Article, Link}

  record Title(String value) implements Value<String, Title> {
    public Title { assert value.length() > 2 && value.length() <= 85; }

    public static One<Title> of(final String value) {
      return One.maybe(value)
        .where(it -> it.length() > 2 && it.length() <= 85)
        .select(Title::new);
    }
  }
  record Text(String value) implements Value<String, Text> {
    public Text { assert value.length() > 2 && value.length() <= 4000; }

    static One<Post.Text> of(final String value) {
      return One.maybe(value)
        .where(it -> it.length() > 2 && it.length() <= 4000)
        .select(Text::new);
    }
  }
  record Link(URL value) implements Value<URL, Link> {
    static One<Post.Link> of(String value) {
      return One.maybe(value)
        .select(URL::new)
        .select(Link::new);
    }
  }

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

