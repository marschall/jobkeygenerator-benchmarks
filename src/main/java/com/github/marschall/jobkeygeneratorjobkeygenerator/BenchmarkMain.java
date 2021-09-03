package com.github.marschall.jobkeygeneratorjobkeygenerator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.openjdk.jmh.results.format.ResultFormatType.TEXT;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkMain {

  public static void main(String[] args) throws RunnerException, IOException, Exception {
    if (args.length != 1) {
      System.err.println("usage: JDK-version");
      System.exit(1);
    }
    String jdkVersion = args[0];
    String resultFile = "jmh-result-JDK" + jdkVersion + ".txt";
    Options options = new OptionsBuilder()
        .include("com\\.github\\.marschall\\.jobkeygeneratorjobkeygenerator\\..*Benchmarks")
        .forks(1)
        .warmupIterations(3)
        .measurementIterations(5)
        .result(resultFile)
        .resultFormat(TEXT)
        .addProfiler("gc")
        .addProfiler("jfr", "debugNonSafePoints=true")
        .build();
    new Runner(options).run();
    
    renameFiles(jdkVersion);
  }
  
  private static void renameFiles(String jdkVersion) throws IOException {
    Path outputDirectory = Files.createDirectories(Paths.get("src", "main", "jfr"));
    String directoryRegex = "com\\.github\\.marschall\\.jobkeygeneratorjobkeygenerator\\.JobKeyGeneratorBenchmarks\\.generateKey\\-AverageTime\\-(jobParameterCount\\-\\d+)\\-(keyGeneratorType\\-\\w+)";
    Pattern directoryPattern = Pattern.compile(directoryRegex);
    try (Stream<Path> directoryStream = Files.list(Paths.get(""))) {
      directoryStream
        .filter(Files::isDirectory)
        .filter(path -> directoryPattern.matcher(path.getFileName().toString()).matches())
        .map(path -> path.resolve("profile.jfr"))
        .filter(Files::exists)
        .forEach(recording -> {
          Matcher matcher = directoryPattern.matcher(recording.getParent().getFileName().toString());
          if (matcher.matches()) {
            String parameterCount = matcher.group(1);
            String generatorType = matcher.group(2);
            String fileName = generatorType + "-" + parameterCount + "-JDK" + jdkVersion + ".jfr";
            Path target = outputDirectory.resolve(fileName);
            try {
              Files.copy(recording, target, REPLACE_EXISTING);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          }
        });
    }
  }

}
