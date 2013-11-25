

const int buttonPin = 2;
const int ledPin =  13;
int buttonState = 2;
int count = 1;

void setup() {
    pinMode(buttonPin, INPUT);     

  Serial.begin(9600);
}
void loop() {
    buttonState = digitalRead(buttonPin);
    
    
      Serial.println("B1");
    
  delay(1000);
}

