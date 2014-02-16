#include <SoftwareSerial.h>

#include <SoftwareSerial.h>

int right = 7; //RED
int left = 8; //ORANGE

int btRX = 11;
int btTX = 10;

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
  btSerial.println("testing456");
}
