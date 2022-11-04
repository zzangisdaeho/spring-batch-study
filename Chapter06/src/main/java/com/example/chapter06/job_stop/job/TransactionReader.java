package com.example.chapter06.job_stop.job;

import com.example.chapter06.job_stop.domain.Transaction;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.transform.FieldSet;

public class TransactionReader implements ItemStreamReader<Transaction> {

    private ItemStreamReader<FieldSet> fieldSetReader;
    private int recordCount = 0;
    private int expectedRecordCount = 0;

    public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    @Override
    public Transaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return process(fieldSetReader.read());
    }

    private Transaction process(FieldSet read) {
        Transaction result = null;

        if(read != null){
            if(read.getFieldCount() > 1){
                result = new Transaction();
                result.setAccountNumber(read.readString(0));
                result.setTimestamp(read.readDate(1, "yyyy-MM-DD HH:mm:ss"));
                result.setAmount(read.readDouble(2));

                recordCount++;
            }else{
                expectedRecordCount = read.readInt(0);
            }

        }
        return result;
    }

    public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader){
        this.fieldSetReader = fieldSetReader;
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution){
        if(recordCount == expectedRecordCount){
            return stepExecution.getExitStatus();
        }else{
            return ExitStatus.STOPPED;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.fieldSetReader.close();
    }
}
