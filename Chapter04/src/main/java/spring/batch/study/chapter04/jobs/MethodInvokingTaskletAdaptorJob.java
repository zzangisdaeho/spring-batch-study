package spring.batch.study.chapter04.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;

import java.util.concurrent.Callable;

@Configuration
@Slf4j
public class MethodInvokingTaskletAdaptorJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

//	@Bean
	public Job methodInvokingJob() {
		return this.jobBuilderFactory.get("methodInvokingJob")
				.start(methodInvokingStep())
				.incrementer(new DailyJobTimeStamper())
				.build();
	}

	@Bean
	public Step methodInvokingStep() {
		return this.stepBuilderFactory.get("methodInvokingStep")
				.tasklet(methodInvokingTasklet(null))
				.build();
	}

	@StepScope
	@Bean
	public MethodInvokingTaskletAdapter methodInvokingTasklet(
			@Value("#{jobParameters['message']}") String message) {

		MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
				new MethodInvokingTaskletAdapter();

		methodInvokingTaskletAdapter.setTargetObject(service());
		methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
		methodInvokingTaskletAdapter.setArguments(
				new String[]{message});

		return methodInvokingTaskletAdapter;
	}

	@Bean
	public CustomService service() {
		return new CustomService();
	}

	@Slf4j
	public static class CustomService{
		public void serviceMethod(String message){
			System.out.println("service method invoked. message : " + message);
			log.info("service method invoked. message : {}", message);
		}

	}
}
