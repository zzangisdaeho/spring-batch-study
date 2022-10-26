package spring.batch.study.chapter04.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;

@Configuration
@Slf4j
public class SystemCommandTaskletJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("systemCommandJob")
				.start(systemCommandStep())
				.incrementer(new DailyJobTimeStamper())
				.build();
	}

	@Bean
	public Step systemCommandStep() {
		return this.stepBuilderFactory.get("systemCommandStep")
				.tasklet(systemCommandTasklet())
				.build();
	}

	@Bean
	public SystemCommandTasklet systemCommandTasklet() {
		SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();

		systemCommandTasklet.setCommand("rm -rf /tmp.txt");
		systemCommandTasklet.setTimeout(5000);
		systemCommandTasklet.setInterruptOnCancel(true);

		return systemCommandTasklet;
	}
}
