# spark-bench-custom-workload

This is a full-fledged example of a working custom workload for [SparkTC/spark-bench](https://github.com/SparkTC/spark-bench).

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [What This Example Does](#what-this-example-does)
- [Try It Out!](#try-it-out)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


## What This Example Does

In this example, I created a custom data generation workload that will take in a string and make a dataset consisting of however
many rows and columns you want of just that string.

From this configuration: 
```text
  {
    name = "custom"
    class = "com.example.WordGenerator"
    output = "console"
    rows = 10
    cols = 3
    word = "Cool stuff!!"
  }
```

I get this result:
```text
+------------+------------+------------+
|           0|           1|           2|
+------------+------------+------------+
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
|Cool stuff!!|Cool stuff!!|Cool stuff!!|
+------------+------------+------------+

```

## Try It Out!

This repo includes the source code, build.sbt file, and example configuration file for creating and using a custom workload.

0. Install spark-bench and sbt.
1. Clone this repo and `cd` into the directory.
2. Change the value of `sparkBenchPath` in build.sbt to reflect your environment.
3. Run `sbt package`. This will create a jar in `target/scala-2.11/`. DO NOT use sbt assembly for custom workloads!
4. Move this new jar into the `lib` folder of your spark-bench installation.
5. Change the value of `driver-class-path` in custom-workload-example.conf to reflect your environment
6. ./path/to/your/spark-bench/installation/bin/spark-bench.sh ./bin/custom-workload-example.conf
