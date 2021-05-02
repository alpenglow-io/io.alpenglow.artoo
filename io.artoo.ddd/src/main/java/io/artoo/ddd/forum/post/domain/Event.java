package io.artoo.ddd.forum.post.domain;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.forum.member.Member;
import io.artoo.ddd.forum.post.Post;
import io.artoo.lance.tuple.Pair;

public sealed interface Event permits Post {
  sealed interface Fact extends Domain.Fact {}

  record ArticleCreated(Post.Title title, Post.Text text) implements Fact, Pair<Post.Title, Post.Text> {}
  record ArticleSigned(Id memberId, Member.FullName fullName) implements Fact, Pair<Id, Member.FullName> {}
}
