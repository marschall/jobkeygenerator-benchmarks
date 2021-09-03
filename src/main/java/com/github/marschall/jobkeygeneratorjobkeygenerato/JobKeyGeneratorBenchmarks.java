package com.github.marschall.jobkeygeneratorjobkeygenerato;

import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.springframework.batch.core.DefaultJobKeyGenerator;
import org.springframework.batch.core.JobKeyGenerator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import com.github.marschall.streamingjobkeygenerator.StreamingJobKeyGenerator;

@BenchmarkMode(AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Benchmark)
public class JobKeyGeneratorBenchmarks {

  private static final String GENERATOR_TYPE_DEFAULT = "default";

  private static final String GENERATOR_TYPE_STREAMING = "streaming";

  @Param({GENERATOR_TYPE_DEFAULT, GENERATOR_TYPE_STREAMING})
  String keyGeneratorType;
  
//  @Param({"0", "1", "2", "4", "8", "16"})
  @Param({"1"})
  int jobParameterCount;
  
  private JobKeyGenerator<JobParameters> keyGenerator;
  
  private JobParameters jobParameters;

  @Setup
  public void doSetup() {
    switch (this.keyGeneratorType) {
      case GENERATOR_TYPE_STREAMING:
        this.keyGenerator = new StreamingJobKeyGenerator();
        break;
      case GENERATOR_TYPE_DEFAULT:
        this.keyGenerator = new DefaultJobKeyGenerator();
        break;
    }
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    for (int i = 0; i < this.jobParameterCount; i++) {
      jobParametersBuilder.addString("parameterKey" + i, "parameterValue" + i);
    }
    this.jobParameters = jobParametersBuilder.toJobParameters();
  }

  @Benchmark
  public String generateKey() {
    return this.keyGenerator.generateKey(this.jobParameters);
  }

}
