#include <SoftwareSerial.h>

//int right = 6; //RED
//int left = 5; //ORANGE

int right = 5; //RED
int left = 6; //ORANGE*/

int btRX = 11;
int btTX = 10;

char current = 'q';
char LEFT_CHAR = 'l';
char RIGHT_CHAR = 'r';

SoftwareSerial btSerial(btRX, btTX);;

void setup() {
  // put your setup code here, to run once:
  pinMode(left, OUTPUT);
  pinMode(right, OUTPUT);
  //btSerial.begin(9600);
  Serial.begin(9600);
}

void loop() {
  // put your main code hre, to run repeatedly:
  //delay(8000);
  //digitalWrite(right, LOW);
  //digitalWrite(left, LOW);
  //delay(8000);
  //digitalWrite(left, LOW);
  //digitalWrite(right, LOW);*/
  //btSerial.println("testing456");
  
       //digitalWrite(left, HIGH);
       //digitalWrite(right, HIGH);
       
  if(Serial.available()){
    current = Serial.read();
    if(current == LEFT_CHAR){
       //Serial.write(current);
       digitalWrite(left, HIGH);
       delay(1000);
       digitalWrite(left, LOW);
       delay(8000);
    }
    else if(current == RIGHT_CHAR){
      //Serial.write(current);
       digitalWrite(right, HIGH);
       delay(2000);
       digitalWrite(right, LOW);
       delay(8000);
    }
    //btSerial.println("back");
  Serial.write(current);
  current = 'q';
  }
}
