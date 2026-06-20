# Reactive AI Agent for Fraud Detection

A reactive AI system that detects fraudulent financial transactions in real time using Machine Learning, Apache Kafka, Spring Boot, Flask, WebSockets, and SMTP-based email alerts.

The project demonstrates how an AI Agent can operate within a live transaction environment, make fraud predictions, react to suspicious activity, and notify subscribed clients automatically.

## PROJECT GOAL

The objective of this project is to create a Reactive AI Agent capable of operating in a transaction-processing environment.

The agent continuously monitors incoming transactions, identifies potentially fraudulent activities using Machine Learning, sends alerts to subscribed clients when fraud is detected, and streams live predictions to a web-based dashboard for monitoring and analysis.

## THINGS THAT I WILL BE LEARNING FROM THS PROJECT:

- Building event-driven systems using Apache Kafka
- Working with Kafka Producers and Consumers
- Deploying Machine Learning models through Flask APIs
- Integrating Machine Learning with distributed systems
- Real-time transaction streaming and processing
- Building reactive AI agents that perceive environments and take actions
- Implementing WebSocket communication using Spring Boot
- Streaming live predictions to frontend applications
- Sending automated email alerts using SMTP
- Designing end-to-end fraud detection pipelines
- Working with Spring Boot as a middleware layer
- Managing communication between Python and Java services
- Deploying full-stack AI applications
- Building deployment-friendly hosted versions of distributed systems

## FRAMEWORKS AND OTHER RESOURCES USED IN THIS PROJECT:

- Backend & Middleware
    - [Java 17](https://www.oracle.com/in/java/technologies/downloads/): Used to build the backend application that coordinates communication between system components.
    - [Spring Boot](https://spring.io/projects/spring-boot): Acts as the middleware layer between the fraud prediction pipeline and the frontend dashboard.
    - [Spring WebSocket](https://docs.spring.io/spring-framework/reference/web/websocket.html): Streams transaction and prediction updates to the frontend in real time.
    - [STOMP](https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html): Provides a messaging protocol over WebSockets for publishing fraud detection events.
    - [SockJS](https://github.com/sockjs/sockjs-client): Ensures reliable browser-to-server WebSocket communication.
    - [Gradle](https://gradle.org/): Used to build, manage dependencies, and run the Spring Boot application.

- Data Streaming
    - [Apache Kafka](https://kafka.apache.org/): Acts as the event streaming platform that transports transactions through the fraud detection pipeline.
    - [Apache ZooKeeper](https://zookeeper.apache.org/): Coordinates and manages the Kafka broker during local deployment.
    - [Kafka Producer](https://kafka.apache.org/41/configuration/producer-configs/): Reads transactions from a CSV file and publishes them to a Kafka topic.
    - [Kafka Consumer](https://kafka.apache.org/41/configuration/consumer-configs/): Consumes streamed transactions, requests fraud predictions, and triggers system actions.

- Machine Learning
    - [Python](https://www.python.org/): Used to implement the machine learning and AI agent components.
    - [Flask](https://flask.palletsprojects.com/en/stable/): Exposes the trained fraud detection model through a REST API.
    - [Scikit-Learn](https://scikit-learn.org/): Provides machine learning utilities used during model training and evaluation.
    - [XGBoost](https://xgboost.ai/): Serves as the fraud detection model that predicts whether a transaction is fraudulent.
    - [Pandas](https://pandas.pydata.org/): Processes and transforms transaction datasets before model inference.
    - [NumPy](https://numpy.org/): Handles numerical operations required by the machine learning pipeline.
    - [Joblib](https://pypi.org/project/joblib/): Loads the trained machine learning model for prediction serving.

- AI Agent Components
    - [SMTP](https://www.smtp.com/): Sends automated fraud alert emails when suspicious transactions are detected.
    - [Gmail App Password Authentication](https://support.google.com/mail/answer/185833?hl=en): Authenticates the AI Agent email account used for fraud notifications.

- Frontend
    - [HTML](https://developer.mozilla.org/en-US/docs/Web/HTML): Provides the user interface for displaying live transactions and fraud predictions.
    - [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS): Styles the fraud monitoring dashboard and prediction cards.
    - [JavaScript](https://developer.mozilla.org/en-US/docs/Web/JavaScript): Handles WebSocket communication and dynamically updates the user interface.
Dataset

- Dataset 
    - [creditcard_2023 Dataset](https://www.kaggle.com/datasets/nelgiriyewithana/credit-card-fraud-detection-dataset-2023): Used to train and evaluate the fraud detection machine learning model.

## REPOSITORY STRUCTURE

### Hosted Deployment

The hosted deployment provides a simplified public demonstration of the project. All the files related to this deployment are found in `hosted-deployment`.

Users can:

- Start a hosted simulation
- View live transaction streams
- Observe fraud predictions in real time
- Explore the project without installing Kafka or ZooKeeper

<u>Architecture Diagram for Hosted Deployment</u>:

![architecture_diagram_hosted](assets\architecture_diagram_hosted.png)

### Local Deployment

The local deployment contains the complete architecture. This is the full implementation of this project and can be run locally. All the files related to this deployment are found in `local-deployment`. It includes:

- Apache Kafka
- ZooKeeper
- Flask Machine Learning API
- Kafka Producer
- Kafka Consumer
- Spring Boot Backend
- WebSocket Frontend
- SMTP Email Alerts

<u>Architecture Diagram for Local Deployment</u>:

![architecture_diagram_local](assets\architecture_diagram_local.png)

## DEPLOYMENT

This app has yet to be deployed :/