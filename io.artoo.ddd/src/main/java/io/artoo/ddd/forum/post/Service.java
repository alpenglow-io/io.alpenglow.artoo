package io.artoo.ddd.forum.post;

import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service.EventLog;
import io.artoo.ddd.core.Service.Operation;
import io.artoo.ddd.core.Service.Projection;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public sealed interface Service permits Post {
  interface ArticleCreation extends Operation<Domain.CreateArticle> {}

  sealed interface OpenArticleView extends Projection<Domain.Event> {
    final class Default implements OpenArticleView {
      private final Map<Id, View> views = new HashMap<>();

      @Override
      public Void tryApply(EventLog<Domain.Event> eventLog) {
        final var event = eventLog.eventData();
        if (event instanceof Domain.ArticleCreated articleCreated) {
          views.put(
            eventLog.modelId(),
            new View(
            articleCreated.title(),
            articleCreated.text(),
            eventLog.persistedAt()
              .atZone(ZoneId.of("Europe/Rome"))
              .toLocalDateTime()
              .format(ISO_DATE_TIME)
            )
          );
        }
        return null;
      }

      private record View(Post.Title title, Post.Text text, String createdAt) {}
    }
  }
}
