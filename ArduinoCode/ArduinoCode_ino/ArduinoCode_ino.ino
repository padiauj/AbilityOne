const int buttonL = A5;     
const int buttonM = A3;     
const int buttonR = A4;    

char choice = 'U';

int left = 0;
int mid = 0;
int right = 0;

int leftprev = 1;
int midprev = 1;
int rightprev = 1;

void setup() {
  pinMode(buttonL, INPUT); 
  pinMode(buttonM, INPUT); 
  pinMode(buttonR, INPUT); 
  Serial.begin(9600);  
}

void loop(){
  left = digitalRead(buttonL);
  mid = digitalRead(buttonM);
  right = digitalRead(buttonR);
  
  if (left == LOW) {
    leftprev = 0;
  }
  
  if (mid == LOW) {
    midprev = 0;
  }
  
  if (right == LOW) {
    rightprev = 0;
  }
  
  if (left == HIGH && leftprev == 0) {
    Serial.println("BL");
    leftprev = 1;

  }
  if (mid == HIGH && midprev == 0) {
    Serial.println("BM"); 
    midprev = 1;
   
  }
  if (right == HIGH && rightprev == 0) {
    Serial.println("BR");    
    rightprev = 1;
  }
  
  
   
  delay(50);


}
