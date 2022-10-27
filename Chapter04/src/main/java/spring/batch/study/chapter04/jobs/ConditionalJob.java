package spring.batch.study.chapter04.jobs;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.decider.RandomDecider;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;

@Configuration
public class ConditionalJob {

    @Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Tasklet passTasklet() {
		return (contribution, chunkContext) -> {
			System.out.println("Pass!");
			throw new RuntimeException("Causing a failure");
//			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Tasklet successTasklet() {
		return (contribution, context) -> {
			System.out.println("Success!");
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Tasklet failTasklet() {
		return (contribution, context) -> {
			System.out.println("Failure!");
//			throw new RuntimeException("Causing a failure");
			return RepeatStatus.FINISHED;
		};
	}

//	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("conditionalJob")
//				.incrementer(new DailyJobTimeStamper())
				.start(firstStep())
				//---------------------- 실패하면 fatilStep으로, 이외에는 success로
//				.on(ExitStatus.FAILED.getExitCode()).to(failureStep())
//				.from(firstStep())
//					.on("*").to(successStep())

				//----------------------- 성공, 실패를 Decider에게 위임
//				.next(decider())
//				.from(decider())
//					.on(ExitStatus.FAILED.getExitCode()).to(failureStep())
//				.from(decider())
//					.on("*").to(successStep())

				//------------------------ 실패하면 멈추고 successStep부터 재시작
				.on(ExitStatus.FAILED.getExitCode()).stopAndRestart(failureStep())
				.from(firstStep())
					.on("*").to(successStep())
				.end()
				.build();
	}

	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("firstStep")
				.tasklet(passTasklet())
				.build();
	}

	@Bean
	public Step successStep() {
		return this.stepBuilderFactory.get("successStep")
				.tasklet(successTasklet())
				.build();
	}

	@Bean
	public Step failureStep() {
		return this.stepBuilderFactory.get("failureStep")
				.tasklet(failTasklet())
				.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new RandomDecider();
	}
}
