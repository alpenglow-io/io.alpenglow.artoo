package io.artoo.ddd.forum.post;

import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

import java.net.URL;

public sealed interface Post extends Domain, Service {
  enum Namespace implements Post {}
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
}

