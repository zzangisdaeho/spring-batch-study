package spring.batch.study.chapter04.jobs.chunk_listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkLoggerListenerWithAnnotation {
    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        System.out.println("ChunkListener beforeChunk called");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        System.out.println("ChunkListener afterChunk called");
    }

    @AfterChunkError
    public void afterChunkError(ChunkContext context) {
        System.out.println("ChunkListener afterChunkError called");
    }
}
