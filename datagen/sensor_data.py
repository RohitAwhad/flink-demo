from faker import Faker
import random
from datetime import datetime,timedelta
from faker.providers import python
from jinja2 import Environment, PackageLoader
import json as json
import argparse
from kafka import KafkaProducer
faker = Faker()
faker.add_provider(python)


# Kafka Producer

def generate_sensor(num_of_records: int) -> None:
    for i in range(num_of_records):
        sensor_id = random.randint(1,num_of_records)
        differ_seconds = random.randint(1,20)
        datetime_occurence = (datetime.now()-timedelta(seconds=differ_seconds)).strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]
        temp = faker.pyfloat(left_digits=2,right_digits=2,positive=True)
        op = f"{{\"temperature\":\"{temp}\",\"timestamp\":\"{datetime_occurence}\",\"sensor_id\":\"{sensor_id}\"}}"
        print(op)
        sensor_data = {'timestamp': datetime_occurence, 'temperature': temp, 'sensor_id': sensor_id}
        # env = Environment(loader=PackageLoader('templates'),trim_blocks=True,lstrip_blocks=True,variable_start_string='{~',variable_end_string='~}')
        # template = env.get_template('sensor_data.py.jinja')
        # output = template.render(sensor_data)
        print(sensor_data)
        producer = KafkaProducer(
            bootstrap_servers=['broker:9092'],
            value_serializer=lambda m : json.dumps(m).encode('utf-8')
        )
        producer.send('demo-topic', sensor_data)
        producer.flush()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-nu",
        "--num_sensor_records",
        type=int,
        help="Number of sensor records to generate",
        default=10000,
    )
    args = parser.parse_args()
    generate_sensor(args.num_sensor_records)