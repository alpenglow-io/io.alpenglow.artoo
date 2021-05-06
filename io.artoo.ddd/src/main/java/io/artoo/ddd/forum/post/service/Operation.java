package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Service;
import io.artoo.ddd.core.lookup.Transactions;
import io.artoo.ddd.forum.post.Post;

public sealed interface Operation permits Post {
  static CommitCreateArticle commitCreateArticle(Transactions transactions) {
    return (id, createArticle) -> transactions.state(id).commit(new Post.ArticleCreated(createArticle.title(), createArticle.text()));
  }

  interface CommitCreateArticle extends Service.Operation<Post.CreateArticle> {}
}
