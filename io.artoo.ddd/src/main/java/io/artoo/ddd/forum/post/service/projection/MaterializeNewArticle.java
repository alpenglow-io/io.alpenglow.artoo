package io.artoo.ddd.forum.post.service.projection;

import io.artoo.ddd.core.Service;
import io.artoo.ddd.forum.post.Post;

public final class MaterializeNewArticle implements Service.Projection<Post.Fact> {
  @Override
  public void tryAccept(final Service.EventLog<Post.Fact> factEventLog) throws Throwable {

  }
}
