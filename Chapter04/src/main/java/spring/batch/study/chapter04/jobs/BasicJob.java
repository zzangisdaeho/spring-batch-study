package spring.batch.study.chapter04.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.study.chapter04.jobs.parameter_validator.ParameterValidator;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BasicJob {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

//    @Bean
//    public Job basicJobJob() {
//        return jobBuilderFactory.get("basicJob")
//                .start(basicJobStep())
////                .validator(new ParameterValidator())
//                .validator(new DefaultJobParametersValidator(new String[]{"name"}, new String[]{"age"}))
//                .build();
//    }
//
//    @Bean
//    public Step basicJobStep() {
//        return stepBuilderFactory.get("basicStep")
//                .tasklet((contribution, chunkContext) -> {
//                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
//                    System.out.println(String.format("Hello, %s, World", jobParameters.get("name")));
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
}
