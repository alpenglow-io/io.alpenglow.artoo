package re.artoo.lance.experimental.flow;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

final class Notification<PAYLOAD> implements Channel<PAYLOAD>, Flow.Publisher<PAYLOAD> {
  private final SubmissionPublisher<PAYLOAD> submission;

  Notification(SubmissionPublisher<PAYLOAD> submission) {
    this.submission = submission;
  }

  @Override
  public void subscribe(Flow.Subscriber<? super PAYLOAD> subscriber) {
    submission.subscribe(subscriber);
  }

  @Override
  public Channel<PAYLOAD> notify(PAYLOAD record) {
    submission.offer()
    return this;
  }
}
