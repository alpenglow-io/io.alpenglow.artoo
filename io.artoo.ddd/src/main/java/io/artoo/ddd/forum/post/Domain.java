package io.artoo.ddd.forum.post;

import static io.artoo.ddd.core.Domain.Act;
import static io.artoo.ddd.core.Domain.Fact;

public sealed interface Domain permits Post {
  interface Command extends Act {}
  interface Event extends Fact {}

  record CreateArticle(Post.Title title, Post.Text text) implements Command {}

  record ArticleCreated(Post.Title title, Post.Text text) implements Event {}
}
