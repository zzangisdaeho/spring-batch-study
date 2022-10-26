package spring.batch.study.chapter04.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;

import java.util.concurrent.Callable;

@Configuration
@Slf4j
public class CallableTaskletAdaptorJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//	@Bean
	public Job callableJob() {
		return this.jobBuilderFactory.get("callableJob")
				.start(callableStep())
                .incrementer(new DailyJobTimeStamper())
				.build();
	}

	@Bean
	public Step callableStep() {
		return this.stepBuilderFactory.get("callableStep")
				.tasklet(tasklet())
				.build();
	}

    @Bean
    public Callable<RepeatStatus> callableObject() {
        return () -> {
            System.out.println("This was executed in another thread. name: " + Thread.currentThread().getName());
			log.info("This was executed in another thread");
            return RepeatStatus.FINISHED;
        };
    }

	@Bean
	public CallableTaskletAdapter tasklet() {
		CallableTaskletAdapter callableTaskletAdapter =
				new CallableTaskletAdapter();

		callableTaskletAdapter.setCallable(callableObject());

		return callableTaskletAdapter;
	}
}
