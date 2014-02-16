#include <SoftwareSerial.h>

//int right = 6; //RED
//int left = 5; //ORANGE

int right = 7; //RED
int left = 8; //ORANGE*/

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
  btSerial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  /*delay(8000);
  digitalWrite(right, LOW);
  digitalWrite(left, LOW);
  delay(8000);
  digitalWrite(left, LOW);
  digitalWrite(right, LOW);*/
  //btSerial.println("testing456");
  
       //digitalWrite(left, HIGH);
       //digitalWrite(right, HIGH);
       /*
  if(btSerial.available()){
    current = btSerial.read();
    if(current == LEFT_CHAR){
       digitalWrite(left, HIGH);
       delay(1000);
       digitalWrite(left, LOW);
       delay(8000);
       current = 'q';
    }
    else if(current == RIGHT_CHAR){
       digitalWrite(right, HIGH);
       delay(1000);
       digitalWrite(right, LOW);
       delay(8000);
       current = 'q';
    }
    btSerial.println("back");
  }
  */
  btSerial.println("gogogo");
}
