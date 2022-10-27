package spring.batch.study.chapter04.jobs.chunk_listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkLoggerListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println("ChunkListener beforeChunk called");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println("ChunkListener afterChunk called");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println("ChunkListener afterChunkError called");
    }
}
