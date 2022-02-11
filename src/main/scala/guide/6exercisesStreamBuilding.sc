import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

Stream(1,0).repeat.take(6).toList

Stream(1,2,3).drain.toList // strips all output from a stream

// exec: As a result, the returned stream emits no elements and hence has output type INothing
Stream.exec(IO.println("!!")).compile.toVector.unsafeRunSync()  // runs an effect and ignores its output

(Stream(1,2) ++ Stream(3).map(_ => throw new Exception("nooo!!!"))).attempt.toList