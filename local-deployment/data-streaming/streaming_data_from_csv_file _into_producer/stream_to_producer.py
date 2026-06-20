import os
from kafka import KafkaProducer
import pandas as pd
import json
import time

print("PRODUCER SCRIPT STARTED")

# Kafka Producer Configuration
producer = KafkaProducer(
    # bootstrap_servers='localhost:9092', # LOCALHOST
    bootstrap_servers=os.getenv("KAFKA_BOOTSTRAP_SERVERS"), # DEPLOYMENT
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Load Transactions CSV

BASE_DIR = os.path.dirname(__file__)

csv_path = os.path.join(
    BASE_DIR,
    "content",
    "creditcard_test.csv"
)

# Stream Transactions One by One
for _, row in df.iterrows():

    message = row.to_dict()

    producer.send(
        "fraud_transactions",
        value=message
    )

    print(f"Sent: {message}")

    # Simulate real-time transaction flow
    time.sleep(1)

producer.flush()
producer.close()

print("✅ Transaction Simulation Completed")