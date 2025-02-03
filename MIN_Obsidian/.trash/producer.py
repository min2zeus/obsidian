from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092')

producer.send('rep3-topic', 'Apache Kafka is a distributed streaming platform - Python TEST'.encode('utf-8'))
