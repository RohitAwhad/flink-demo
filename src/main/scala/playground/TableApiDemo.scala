package playground

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}
import org.apache.flink.connector.datagen.table.DataGenConnectorOptions
import org.apache.flink.table.api.{AnyWithOperations, DataTypes, EnvironmentSettings, FieldExpression, Schema, TableDescriptor, TableEnvironment, col}
import org.apache.flink.table.types.DataType

object TableApiDemo {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val olympic_data = env.readCsvFile[olympic]("src/main/resources/data/olympic-athletes.csv")
    //olympic_data.print()
    val setting = EnvironmentSettings.newInstance().inBatchMode().build()
    val tEnv = TableEnvironment.create(setting)
    val source_ddl = """
        create table olympic(
            playerName VARCHAR,
            country VARCHAR,
            year_int BIGINT,
            game VARCHAR,
            gold BIGINT,
            silver BIGINT,
            bronze BIGINT,
            total BIGINT
        ) with (
            'connector' = 'filesystem',
            'format' = 'csv',
            'path' = 'D:\\intellij_projects\\flink\\flink-demo\\src\\main\\resources\\data\\olympic-athletes.csv'
        )
        """
    tEnv.executeSql(source_ddl)

    val sink_ddl = """
    create table results(
        a VARCHAR,
        cnt BIGINT
    ) with (
        'connector' = 'filesystem',
        'format' = 'csv',
        'path' = 'D:\\intellij_projects\\flink\\flink-demo\\src\\main\\resources\\data\\hhs.csv'
    )
    """
    tEnv.executeSql(sink_ddl)

    tEnv.from("olympic").groupBy(col("country")).select(col("country").as("a"),col("total").sum.as("cnt")).executeInsert("results")



    /* val tableDescriptor= TableDescriptor.forConnector("filesystem").format("csv").schema(Schema.newBuilder().column("playername",DataTypes.STRING()).column("country",DataTypes.STRING()).column("year",DataTypes.BIGINT()).column("player",DataTypes.STRING()).column("country",DataTypes.BIGINT()).column("gold",DataTypes.STRING()).column("silver",DataTypes.BIGINT()).column("bronze",DataTypes.BIGINT()).column("total",DataTypes.BIGINT()).build()).
       .build()
     val atheltes = tEnv.createTable("olympic",tableDescriptor)
     atheltes.*/
    /*final TableDescriptor sourceDescriptor = TableDescriptor.forConnector("datagen")
      .schema(Schema.newBuilder()
        .column("f0", DataTypes.STRING())
        .build())
      .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
      .build();

    tableEnv.createTable("SourceTableA", sourceDescriptor);*/
  }

}

case class olympic(playerName:String,country:String,year:Int,game:String,gold:Int,silver:Int,bronze:Int,total:Int)
