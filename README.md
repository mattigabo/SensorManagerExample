# SensorManagerExample


This is an example of a little application that manage different sensor stations using multiple services that interacts with AMQP and expose a REST API interface.  

There are two type of services:

#### The first type is the "SensorStation".  
  This service represents a service that manage a set of sensors mounted in a room.
  The service read data from sensors send it to another service ("SensorManager") through RabbitMQ. 
  Sensor are moked internally at the service with random generators)
  Every station have 2 sensors:  (Oxygen, Humidity)
  In this example we is used the topic exchange type of RabbitMQ where the routing-key is composed by sensor.station-name (e.g. oxygen.station1)
  
#### The second type of service will be the "SensorManager".
  This service have two purpose:
    
 - Collect data from the registered "SensorStation"s through RabbitMQ
 - The service keeps in memory only 10 last values of each sensors (you can choose what data structure use e.g list, queue, set ecc..)
 - Offers a REST interface that can be used by a generic User Application:
    -  Add a new station, when this api is used the station manager add a listener to the value sended from the new station.  
    The name of thestation will be sended in the body of the message
        - POST:http://{hostname}:{port}/addstation 
    - Get all station info (name + current sensors values)
        - GET:http://{hostname}:{port}/stations/
    - Get info of a specific station
        - GET:http://{hostname}:{port}/stations/**station-name**
    - Get the sensor values of a station
        - GET:http://{hostname}:{port}/stations/**station-name**/**sensor-name**/ 
    
    
    
In the file "UserInteractionSimulator.kt" there is a function that simulate a generic User App that interact with the REST interface of the "SensorManager".    

This is one of the code examples created for the course in "Distributed Systems".

authors: Matteo Gabellini & Elia Di Pasquale