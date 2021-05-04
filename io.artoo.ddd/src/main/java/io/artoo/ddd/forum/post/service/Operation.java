package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Service;
import io.artoo.ddd.core.lookup.Store;
import io.artoo.ddd.forum.post.Post;

public sealed interface Operation permits Post {
  static CommitCreateArticle commitCreateArticle(Store store) {
    return (id, createArticle) -> store.state(id).commit(new Post.ArticleCreated(createArticle.title(), createArticle.text()));
  }

  interface CommitCreateArticle extends Service.Operation<Post.CreateArticle> {}
}
