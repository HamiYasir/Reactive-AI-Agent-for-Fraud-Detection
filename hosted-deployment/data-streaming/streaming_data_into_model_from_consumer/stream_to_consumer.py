import os
from dotenv import load_dotenv

load_dotenv()

import requests
import json
import time
import smtplib

from kafka import KafkaConsumer
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

# =========================
# API CONFIGURATION
# =========================

API_URL = os.getenv("API_URL")
SPRING_BOOT_API = os.getenv("SPRING_BOOT_API")

# =========================
# SMTP CONFIGURATION
# =========================

SMTP_SERVER = os.getenv("SMTP_SERVER")
SMTP_PORT = int(os.getenv("SMTP_PORT"))

SMTP_USERNAME = os.getenv("SMTP_USERNAME")
SMTP_PASSWORD = os.getenv("SMTP_PASSWORD")

EMAIL_SENDER = SMTP_USERNAME
EMAIL_RECEIVER = os.getenv("EMAIL_RECEIVER")

# =========================
# FRAUD ALERT SETTINGS
# =========================

fraud_email_count = 0
FRAUD_EMAIL_LIMIT = 3


def send_alert(transaction, probability):
    """Send fraud alert email"""

    subject = "Fraud Alert: Suspicious Transaction Detected!"

    body = f"""
    <html>
    <body>
        <h2>Fraud Alert Detected!</h2>

        <p><b>Transaction Details:</b></p>
        <pre>{transaction}</pre>

        <p><b>Fraud Probability:</b> {probability:.4f}</p>

        <p><b>Action:</b> Blocked ✅</p>
    </body>
    </html>
    """

    msg = MIMEMultipart("alternative")

    msg["Subject"] = subject
    msg["From"] = f"Fraud Detection <{EMAIL_SENDER}>"
    msg["To"] = EMAIL_RECEIVER

    msg.attach(MIMEText(body, "html"))

    try:

        server = smtplib.SMTP(
            SMTP_SERVER,
            SMTP_PORT
        )

        server.starttls()

        server.login(
            SMTP_USERNAME,
            SMTP_PASSWORD
        )

        server.sendmail(
            EMAIL_SENDER,
            EMAIL_RECEIVER,
            msg.as_string()
        )

        server.quit()

        print("✅ Alert sent via SMTP")

    except Exception as e:

        print(f"Email alert failed: {e}")


# =========================
# KAFKA CONSUMER
# =========================

import time

consumer = KafkaConsumer(
    "fraud_transactions",
    # bootstrap_servers="localhost:9092", # LOCALHOST
    bootstrap_servers=os.getenv("KAFKA_BOOTSTRAP_SERVERS"), # DEPLOYMENT
    auto_offset_reset="earliest",
    group_id=f"fraud-detection-{int(time.time())}",  # fresh group each run
    value_deserializer=lambda m: json.loads(m.decode("utf-8"))
)

print("Consumer Started")
print("Waiting for Kafka Messages...\n")

# =========================
# PROCESS TRANSACTIONS
# =========================

for message in consumer:

    try:

        transaction_data = message.value

        print(
            f"Received: {transaction_data}"
        )

        response = requests.post(
            API_URL,
            json=transaction_data
        )

        if response.status_code != 200:

            print(
                f"Prediction API Error: {response.text}"
            )

            continue

        prediction = response.json()

        print(
            f"Prediction: {prediction}"
        )

        # Fraud Handling
        if prediction[0]["fraud"]:

            if fraud_email_count < FRAUD_EMAIL_LIMIT:

                send_alert(
                    transaction_data,
                    prediction[0]["probability"]
                )

                fraud_email_count += 1

            else:

                print(
                    "Fraud email limit reached"
                )

        # Send prediction to Spring Boot
        websocket_response = requests.post(
            SPRING_BOOT_API,
            json={
                "transaction": transaction_data,
                "prediction": prediction
            }
        )

        if websocket_response.status_code == 200:

            print(
                "Sent data to Spring Boot WebSocket"
            )

        else:

            print(
                f"Spring Boot Error: "
                f"{websocket_response.text}"
            )

    except Exception as e:

        print(
            f"CONSUMER EXCEPTION: {e}"
        )

    time.sleep(1)