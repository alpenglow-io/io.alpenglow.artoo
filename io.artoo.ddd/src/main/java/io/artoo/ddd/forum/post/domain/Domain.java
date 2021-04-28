package io.artoo.ddd.forum.post.domain;

import io.artoo.ddd.forum.post.Post;

import static io.artoo.ddd.core.Domain.Act;
import static io.artoo.ddd.core.Domain.Fact;

public sealed interface Domain permits Post {
  record CreateArticle(Post.Title title, Post.Text text) implements Act {}

  record ArticleCreated(Post.Title title, Post.Text text) implements Fact {}
}
