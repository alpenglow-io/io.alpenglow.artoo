package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service;
import io.artoo.ddd.forum.post.Post;

public interface ArticleCreation extends Service.Operation<Post.CreateArticle> {
  static ArticleCreation

  final class Handler implements ArticleCreation {
    private final Service.Model model;

    private Handler(Service.Model model) {
      this.model = model;
    }

    @Override
    public Void tryApply(final Id id, final Post.CreateArticle createArticle) throws Throwable {
      model.open(id).commit(facts -> facts.concat(new Post.ArticleCreated(createArticle.title(), createArticle.text())));
      return null;
    }
  }
}
