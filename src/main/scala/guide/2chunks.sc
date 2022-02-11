// FS2 streams are chunked internally for performance

import fs2.{Stream, Chunk}

val s1c = Stream.chunk(Chunk.array(Array(1.0, 2.0, 3.0)))

s1c.toList