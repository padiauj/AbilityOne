char choice = 'U';
const int[] button = {A5,A4,A3};
int[] value = {0,0,0};
int[] previous = {1,1,1};


void setup() {
  for (int i=0; i<button.length; i++) {
    pinMode(button[i],INPUT);
  }

  Serial.begin(9600);  
}

void loop(){
  
  for (int i=0; i<button.length; i++) {
    value[i] = digitalRead(button[i]);
  }

  for (int i=0; i<button.length;i++) {
    if (value[i] == LOW) {
      previous[i] = 0;
    }
    else {
     if (previous[i] == 0) {
       Serial.println(i);
       previous[i] = 1;
     }
    }
  }
    
  delay(50);
}
