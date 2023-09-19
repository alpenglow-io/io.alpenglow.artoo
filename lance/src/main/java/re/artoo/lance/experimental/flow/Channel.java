package re.artoo.lance.experimental.flow;

import java.security.SecureRandom;
import java.util.List;

public interface Channel<PAYLOAD> {
  static <PAYLOAD> Channel<PAYLOAD> ofRecords() {
    return new Notification<>();
  }

  Channel<PAYLOAD> notify(PAYLOAD PAYLOAD);

  interface Event {}
}

interface Person {
  sealed interface Event extends Channel.Event {
    record Registered() implements Event {}

    record Unregistered() implements Event {}
  }
}

interface Service {
  sealed interface Event extends Channel.Event {
    record Online() implements Event {}

    record Offline() implements Event {}
  }
}


interface Main {
  static void main(String[] args) throws InterruptedException {
    var random = new SecureRandom();
    var channel =
      new Notification(
        new Routing<>(List.of(new PersonUnit())),
        new Routing<>(List.of(new ServiceUnit()))
      );


    boolean isRunning = true;
    while (isRunning) {
      random.ints(1024, 0, 32).forEach(it -> {
        if (it % 2 == 0)
          channel.notify(new Service.Event.Online());
        else if (it % 3 == 0)
          channel.notify(new Service.Event.Offline());
        else if (it % 5 == 0)
          channel.notify(new Person.Event.Registered());
        else if (it % 7 == 0)
          channel.notify(new Person.Event.Unregistered());
      });
      isRunning = false;
      Thread.sleep(1000);
    }
  }
}

