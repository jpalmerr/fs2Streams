import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global

Stream(1,2,3).merge(Stream.eval(IO { Thread.sleep(200); 4 })).compile.toVector.unsafeRunSync()

/*
merge (runs two streams concurrently, combining their outputs. It halts when both inputs have halted)
concurrently (runs another stream concurrently and discards its output)
interruptWhen (halts if the left branch produces true)
either (like merge but returns an Either)
mergeHaltBoth (halts if either branch halts)
parJoin (runs multiple streams concurrently) -- most popular at work with kafka streams
*/




