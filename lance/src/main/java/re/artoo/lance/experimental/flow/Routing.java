package re.artoo.lance.experimental.flow;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicReference;

final class Routing<EVENT extends Channel.Event> extends SubmissionPublisher<EVENT> implements Flow.Processor<Channel.Event, EVENT> {
  private final AtomicReference<Flow.Subscription> subscription = new AtomicReference<>();
  private final List<Flow.Subscriber<? super EVENT>> subscribers;
  private final Class<EVENT> type;

  @SuppressWarnings("unchecked")
  @SafeVarargs
  Routing(List<Flow.Subscriber<? super EVENT>> subscribers, EVENT... events) {
    this.subscribers = subscribers;
    this.type = (Class<EVENT>) events.getClass().getComponentType();
    System.out.println("Type is " + type.getCanonicalName());
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription.set(subscription);
    if (!hasSubscribers())
      for (var subscriber : subscribers) {
        subscribe(subscriber);
      }

    subscription.request(32);
  }

  @Override
  public void onNext(Channel.Event item) {
    subscription.get().request(32);
    if (type.isInstance(item)) {
      submit(type.cast(item));
    }
  }

  @Override
  public void onError(Throwable throwable) {
    closeExceptionally(throwable);
  }

  @Override
  public void onComplete() {
    close();
  }
}
