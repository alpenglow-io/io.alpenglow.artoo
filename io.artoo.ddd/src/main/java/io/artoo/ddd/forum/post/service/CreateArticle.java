package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service;
import io.artoo.ddd.forum.post.Post;

public interface CreateArticle extends Service.Operation<CreateArticle.Command> {
  record Command(Id modelId, Post.Title title, Post.Text text) implements Domain.Act {}

  final class Transactional implements CreateArticle {
    private final Service.Transaction transaction;

    private Transactional(Service.Transaction transaction) {
      this.transaction = transaction;
    }

    @Override
    public Void tryApply(Id id, Command command) throws Throwable {
      transaction.commit()
      return null;
    }
  }
}
