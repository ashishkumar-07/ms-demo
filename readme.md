winpty docker run -it --network=host edenhill/kcat:1.7.1 kcat -b localhost:19092  -t twitter-topic -s value=avro -r http://localhost:8081