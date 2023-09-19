package re.artoo.lance.experimental.flow;

import java.util.concurrent.Flow;

final class PersonUnit implements Flow.Subscriber<Person.Event> {
  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    (this.subscription = subscription).request(32);
  }

  @Override
  public void onNext(Person.Event item) {
    subscription.request(32);
    switch (item) {
      case Person.Event.Registered registered -> System.out.println("Person has been registered");
      case Person.Event.Unregistered unregistered -> System.out.println("Person has been unregistered");
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
