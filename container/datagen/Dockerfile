FROM python:3.8-slim

RUN pip install \
    psycopg2-binary==2.9.3 \
    faker \
	kafka-python==2.0.2 


WORKDIR /opt/datagen

COPY requirements.txt /opt/datagen/

RUN pip install --no-cache-dir -r /opt/datagen/requirements.txt

CMD ["tail" "-F" "anything"]