// Sometimes some tasks have to run only when some conditions are met or until some other task completes

import fs2.Stream
import cats.effect.{Deferred, IO}
import cats.effect.unsafe.implicits.global
import scala.concurrent.duration.*

val program =
  // Deferred is a concurrency primitive that represents a condition yet to be fulfilled
  Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
    val switcher =
      Stream.eval(switch.complete(())).delayBy(5.seconds)

    val program =
      Stream.repeatEval(IO(println(java.time.LocalTime.now))).metered(1.second)
      // metered, on the other hand, forces our stream to emit values at the specified rate

    program
      .interruptWhen(switch.get.attempt) // // obtaining a stream that will stop evaluation as soon as "the switch gets flipped"
      .concurrently(switcher) // switcher to run in background
  }

program.compile.drain.unsafeRunSync()

// this is such a common use case there is a method defined

val program1 =
  Stream.
    repeatEval(IO(println(java.time.LocalTime.now))).
    metered(1.second).
    interruptAfter(5.seconds)

program1.compile.drain.unsafeRunSync()