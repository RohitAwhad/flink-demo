package playground

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import part4io.KafkaIntegration.{env, main}
import schema.{Sensor, SensorSchema}

import java.time.Duration

object SensorAnalytics {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaSource = KafkaSource.builder[Sensor]()
      .setBootstrapServers("localhost:19092")
      .setTopics("demo-topic")
      .setGroupId("events-group")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SensorSchema())
      .build()


    val kafkaStrings: DataStream[Sensor] = env.fromSource(kafkaSource, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)), "Kafka Source")

    /*val sensor_data = env.fromElements((1705319800,43.2,1),(1700319800,60.3,1),(1704319800,48.2,1),(1700000000,47.7,1))
    val withTimestampsAndWatermarks = sensor_data.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofDays(20)))*/
    // use the DS
    //kafkaStrings.flatMap(_.split(","))
    val aggData = kafkaStrings.map(sensor => (sensor.sensor_id,sensor.temperature,1))
      .keyBy( tuple => tuple._1)
      //.window(TumblingEventTimeWindows.of(Time.seconds(10)))
      .reduce{(left, right) => (left._1, left._2 + right._2, left._3 + right._3)}
      .map(tuple =>  (tuple._1, tuple._2 / tuple._3))

    aggData.print()
    env.execute()
  }


}
