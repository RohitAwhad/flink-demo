package playground

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}

object WordCount {
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    val amounts = env.fromElements(1,29,40,50)
    val collect = amounts.filter(a=> a>30).reduce((a,b) => a+b).collect()
    println(collect)
    env.execute("ss")
  }
}
