package re.artoo.lance.experimental.flow;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

final class Any<ELEMENT> extends SubmissionPublisher<ELEMENT> implements Channel<ELEMENT>, Flow.Publisher<ELEMENT> {
  @Override
  public Channel<ELEMENT> post(Event event) {
    return null;
  }
}
