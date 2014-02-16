import processing.net.*;
import processing.serial.*;

Serial btSerial;
Server server;

void setup(){
  size(200,200);
  //println(Serial.list());
  server = new Server(this, 80);
  println(server.ip());
  btSerial = new Serial(this, Serial.list()[0], 9600);
}

void draw(){
  Client currClient = server.available();
  if(currClient != null){
    String message = currClient.readString();
    if(message.contains("<start>left</start>")){
      btSerial.write('l');
    }
    else if(message.contains("<start>right</start>")){
      btSerial.write('r');
    }
  }
}
