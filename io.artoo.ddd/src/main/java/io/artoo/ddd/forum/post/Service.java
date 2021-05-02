package io.artoo.ddd.forum.post;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service.EventLog;
import io.artoo.ddd.core.Service.Entity;
import io.artoo.ddd.core.Service.Operation;
import io.artoo.ddd.core.Service.Projection;
import io.artoo.ddd.forum.member.Member;
import io.artoo.ddd.forum.post.service.CommitCreateArticle;
import io.artoo.lance.query.Many;

import static io.artoo.ddd.forum.post.Post.ArticleCreated;
import static io.artoo.ddd.forum.post.Post.CreateArticle;
import static io.artoo.ddd.forum.post.Post.Text;
import static io.artoo.ddd.forum.post.Post.Title;
import static io.artoo.ddd.forum.post.Service.MaterializeNewArticle.NewArticles.Entry;
import static io.artoo.ddd.forum.post.domain.Event.ArticleSigned;

public sealed interface Service permits Post {
  static Operation<CreateArticle> commitCreateArticle(Entity entity) { return new CommitCreateArticle(entity); }

  sealed interface MaterializeNewArticle extends Projection<Domain.Fact> {
    interface NewArticles extends Many<Entry> {
      record Entry(Id id, Title title, Text text, Member.FullName fullName) {
        public Entry(Id id) {this(id, null, null, null); }

        public Entry(Id id, Title title, Text text) {this(id, title, text, null); }
      }
    }

    final class InMemory implements MaterializeNewArticle {
      private final NewArticles newArticles;

      public InMemory(final NewArticles newArticles) {this.newArticles = newArticles;}

      @Override
      public void tryAccept(final EventLog<Domain.Fact> eventLog) throws Throwable {
        final var event = eventLog.eventData();

        newArticles
          .single(entry -> entry.id.is(eventLog.modelId()))
          .or(new Entry(eventLog.modelId()))
          .select(entry ->
            event instanceof ArticleCreated created ? new Entry(entry.id, created.title(), created.text())
              :
              event instanceof ArticleSigned signed ? new Entry(entry.id, entry.title, entry.text, signed.fullName())
                :
                entry
          );
      }
    }
  }
}
