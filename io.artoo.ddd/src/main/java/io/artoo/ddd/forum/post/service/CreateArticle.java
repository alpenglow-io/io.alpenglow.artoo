package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service;
import io.artoo.ddd.forum.post.Post;

public interface CreateArticle extends Service.Operation<CreateArticle.Proposal> {
  record Proposal(Id modelId, Post.Title title, Post.Text text) implements Domain.Act {}

  final class Transactional implements CreateArticle {
    private final Service.Model post;

    private Transactional(Service.Model post) {
      this.post = post;
    }

    @Override
    public Void tryApply(Id id, Proposal proposal) throws Throwable {
      post.open(id).commit(facts -> );
      return null;
    }
  }
}
