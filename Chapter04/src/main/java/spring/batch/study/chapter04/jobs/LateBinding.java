package spring.batch.study.chapter04.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.job_listener.JobLoggerListener;
import spring.batch.study.chapter04.jobs.job_listener.JobLoggerListenerWithAnnotation;
import spring.batch.study.chapter04.jobs.parameter_incrementor.DailyJobTimeStamper;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class LateBinding {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job lateBindingJob() {
        return jobBuilderFactory.get("lateBindingJob")
                .start(lateBindingStep())
                .next(notLateBindingStep())
//                .incrementer(new RunIdIncrementer())
                .incrementer(new DailyJobTimeStamper())
//                .listener(new JobLoggerListener())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListenerWithAnnotation()))
                .build();
    }

    @Bean
    public Step lateBindingStep() {
        return stepBuilderFactory.get("lateBindingStep1")
                .tasklet(lateBindingTasklet(null))
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step notLateBindingStep(){
        return stepBuilderFactory.get("notLateBindingStep1")
                .tasklet(notLateBindingTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet lateBindingTasklet(@Value("#{jobParameters['name']}") String name) {
        return (contribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s, World", name));
            ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
            executionContext.put("age", 30);

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet notLateBindingTasklet(){
        return (contribution, chunkContext) -> {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            System.out.println(String.format("Bye, %s, %d, World", jobParameters.get("name"), chunkContext.getStepContext().getJobExecutionContext().get("age")));
            return RepeatStatus.FINISHED;
        };
    }

    public StepExecutionListener promotionListener(){
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();

        executionContextPromotionListener.setKeys(new String[]{"age"});

        return executionContextPromotionListener;
    }

}
