package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Service;
import io.artoo.ddd.core.lookup.Ledger;
import io.artoo.ddd.forum.post.Post;

public sealed interface Operation permits Post {
  static CommitCreateArticle commitCreateArticle(Ledger ledger) {
    return (id, createArticle) -> ledger.state(id).commit(new Post.ArticleCreated(createArticle.title(), createArticle.text()));
  }

  interface CommitCreateArticle extends Service.Operation<Post.CreateArticle> {}
}
