package re.artoo.lance.experimental.flow;

import java.util.concurrent.Flow;

final class ServiceUnit implements Flow.Subscriber<Service.Event> {
  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    (this.subscription = subscription).request(1);
  }

  @Override
  public void onNext(Service.Event item) {
    subscription.request(32);
    switch (item) {
      case Service.Event.Online online -> System.out.println("Service is online");
      case Service.Event.Offline offline -> System.out.println("Service is offline");
    }
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {
    subscription.cancel();
  }
}
