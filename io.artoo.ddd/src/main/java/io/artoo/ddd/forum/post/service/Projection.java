package io.artoo.ddd.forum.post.service;

import io.artoo.ddd.core.Service;
import io.artoo.ddd.forum.post.Post;
import io.artoo.ddd.forum.post.domain.Event;

public sealed interface Projection permits Post {
  interface MaterializeNewArticle extends Service.Projection<Event> {

  }
}
