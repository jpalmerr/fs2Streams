/*
Sometimes, scanChunksOpt isn't powerful enough to express the stream transformation.
Regardless of how complex the job,
the fs2.Pull type can usually express it.
represents a program that may pull values from one or more streams,
write output of type O, and return a result of type R
 */

import fs2._

// The Pull[F[_],O,R] type represents a program that may pull values from one or more streams
// write output of type O, and return a result of type R
// forms a monad :)

val p1 = Pull.output1(1) // Output(Chunk(1))
val s1 = p1.stream

(p1 >> Pull.output1(2)).stream.toList

val res: Pull[Pure, INothing, Option[(Chunk[Int], Stream[Pure, Int])]] = s1.pull.uncons
// option => non empty if there is a chunk to pyll, no more chunks to pull then None
// Chuck[Int] => pulled chunk
// Stream[Pure, Int] => tail of the stream



def tk[F[_],O](n: Long): Pipe[F,O,O] = {
  def go(s: Stream[F,O], n: Long): Pull[F,O,Unit] = {
    s.pull.uncons.flatMap {     // We use uncons to pull the next chunk from the stream
      case Some((hd,tl)) =>
        hd.size match {
          case m if m <= n => Pull.output(hd) >> go(tl, n - m)
          case _ => Pull.output(hd.take(n.toInt)) >> Pull.done // .done => a pull that does nothing
        }
      case None => Pull.done
    }
  }
  in => go(in,n).stream
}

Stream(1,2,3,4).through(tk(2)).toList

// through: Transforms this stream using the given Pipe

Stream.range(0,100).takeWhile(_ < 7).toList
Stream("Alice","Bob","Carol").intersperse("|").toList

// explicit recursion

def tk2[F[_],O](n: Long): Pipe[F,O,O] = {
  in => in.pull.take(n).void.stream
}

Stream(1,2,3,4).through(tk2(2)).toList