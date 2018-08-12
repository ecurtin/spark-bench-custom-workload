package com.example

import com.ibm.sparktc.sparkbench.workload.{Workload, WorkloadDefaults}
import com.ibm.sparktc.sparkbench.utils.GeneralFunctions._
import com.ibm.sparktc.sparkbench.utils.SaveModes
import com.ibm.sparktc.sparkbench.utils.SparkFuncs.writeToDisk
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

// Create a quick case class with a member for each field we want to return in the results.
case class WordGeneratorResult(
                                name: String,
                                timestamp: Long,
                                generate_time: Long,
                                convert_time: Long,
                                save_time: Long,
                                total_runtime: Long,
                                word: String
                              )


/*
  Each workload must have a companion object extending WorkloadDefaults.  Here, you define required
  constant attributes of the workload like its name, as well as any default values or constants that
  you want to use and a constructor for your workload.
 */
object WordGenerator extends WorkloadDefaults {
  val name = "word-generator"

  /*
    Give the WorkloadDefaults an apply method that constructs your workload  from a
    Map[String, Any]. This will be the form you receive your parameters in from the spark-bench
    infrastructure. Example:
    Map(
      "name" -> "word-generator",
      "output" -> "/tmp/one-word-over-and-over.csv",
      "word" -> "Cool"
    )

    Keep in mind that the keys in your map have been toLowerCase()'d for consistency.
  */

  override def apply(m: Map[String, Any]) = WordGenerator(
    numRows = getOrThrow(m, "rows").asInstanceOf[Int],
    numCols = getOrThrow(m, "cols").asInstanceOf[Int],
    output = Some(getOrThrow(m, "output").asInstanceOf[String]),
    word = getOrThrow(m, "word").asInstanceOf[String]
  )
}


case class WordGenerator(
                          numRows: Int,
                          numCols: Int,
                          input: Option[String] = None,
                          output: Option[String],
                          word: String
                        ) extends Workload {

  override val saveMode = SaveModes.overwrite

  /*
      doWorkload is an abstract method from Workload. It optionally takes input data, and it will
      output a one-row DataFrame made from the results case class we defined above.
  */
  override def doWorkload(df: Option[DataFrame] = None, spark: SparkSession): DataFrame = {
    // Every workload returns a timestamp from the start of its doWorkload() method
    val timestamp = System.currentTimeMillis()

    /*
      The time {} function returns a tuple of the start-to-finish time of whatever function
      or block you are measuring, and the results of that code. Here, it's going to return a tuple
      of the time and the string that's being returned.
      If we don't care about the results, we can assign it to _.
    */
    val (generateTime, strrdd): (Long, RDD[String]) = time {
      val oneRow = Seq.fill(numCols)(word).mkString(",")
      val dataset: Seq[String] = for (i <- 0 until numRows) yield oneRow
      spark.sparkContext.parallelize(dataset)
    }

    val (convertTime, dataDF) = time {
      val rdd = strrdd.map(str => str.split(","))
      val schemaString = rdd.first().indices.map(_.toString).mkString(" ")
      val fields = schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, nullable = false))
      val schema = StructType(fields)
      val rowRDD: RDD[Row] = rdd.map(arr => Row(arr: _*))
      spark.createDataFrame(rowRDD, schema)
    }

    val (saveTime, _) = time { writeToDisk(output.get, saveMode, dataDF, spark) }

    val totalTime = generateTime + convertTime + saveTime

    spark.createDataFrame(Seq(WordGeneratorResult("word-generator", timestamp, generateTime, convertTime, saveTime, totalTime, word)))

  }
}