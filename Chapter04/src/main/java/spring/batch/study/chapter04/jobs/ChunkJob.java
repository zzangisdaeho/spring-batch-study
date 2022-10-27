package spring.batch.study.chapter04.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepListenerFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.chunk_completion_policy.RandomChunkSizePolicy;
import spring.batch.study.chapter04.jobs.chunk_listener.ChunkLoggerListener;
import spring.batch.study.chapter04.jobs.chunk_listener.ChunkLoggerListenerWithAnnotation;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;
import spring.batch.study.chapter04.jobs.step_listener.StepLoggerListener;
import spring.batch.study.chapter04.jobs.step_listener.StepLoggerListenerWithAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class ChunkJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private AtomicInteger chunkNum = new AtomicInteger(1);

//    @Bean
    public Job chunkBasedJob() {
        return this.jobBuilderFactory.get("chunkBasedJob")
                .start(chunkStep())
                .incrementer(new DailyJobTimeStamper())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return this.stepBuilderFactory.get("chunkStep")
//				.<String, String>chunk(100)
//                .<String, String>chunk(completionPolicy())
                .<String, String>chunk(new RandomChunkSizePolicy())
                .reader(itemReader())
                .writer(itemWriter())
//				.listener(new StepLoggerListener())
				.listener(new StepLoggerListenerWithAnnotation())
//                .listener(new ChunkLoggerListener())
                .listener(new ChunkLoggerListenerWithAnnotation())
                .build();
    }

    @Bean
    public ListItemReader<String> itemReader() {
        List<String> items = Stream.generate(() -> UUID.randomUUID().toString()).limit(1000).collect(Collectors.toList());

        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
//            items.forEach(log::info);
//            items.forEach(System.out::println);
            log.info("==========chunked item size : {}===========", items.size());
            log.info("==========chunk num : {}===========", chunkNum.getAndIncrement());
        };
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        CompositeCompletionPolicy policy =
                new CompositeCompletionPolicy();

        policy.setPolicies(
                new CompletionPolicy[]{
                        new TimeoutTerminationPolicy(2),
                        new SimpleCompletionPolicy(100)});

        return policy;
    }

//	@Bean
//	public CompletionPolicy randomCompletionPolicy() {
//		return new RandomChunkSizePolicy();
//	}
}
