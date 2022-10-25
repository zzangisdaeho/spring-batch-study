package spring.batch.study.chapter04.jobs.job_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobLoggerListener implements JobExecutionListener {

    private static String START_MESSAGE = "%s is beginning execution";
    private static String END_MESSAGE = "%s has completed with the status %s";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(String.format(START_MESSAGE, jobExecution.getJobInstance().getJobName()));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(String.format(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus()));
    }
}
