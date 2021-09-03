package com.github.marschall.jobkeygeneratorjobkeygenerato;

import static org.openjdk.jmh.results.format.ResultFormatType.TEXT;

import java.io.IOException;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkMain {

  public static void main(String[] args) throws RunnerException, IOException, Exception {
    Options options = new OptionsBuilder()
        .include("com\\.github\\.marschall\\.jobkeygeneratorjobkeygenerator\\..*Benchmarks")
        .forks(1)
        .warmupIterations(3)
        .measurementIterations(5)
        .resultFormat(TEXT)
        .addProfiler("gc")
        .addProfiler("jfr", "debugNonSafePoints=true")
        .build();
    new Runner(options).run();

  }

}
