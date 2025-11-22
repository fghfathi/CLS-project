const byte buttonPin0 = 2;  // the number of the pushbutton pin
const byte buttonPin1 = 3;  // the number of the pushbutton pin
const byte buttonPin2 = 4;  // the number of the pushbutton pin
const byte buttonPin3 = 5;  // the number of the pushbutton pin
const byte buttonPin4 = 6;  // the number of the pushbutton pin
const byte buttonPin5 = 7;  // the number of the pushbutton pin
const short buttonPin6 = 8;  // the number of the pushbutton pin
const short buttonPin7 = 9;  // the number of the pushbutton pin

const short ledPin0 = 10; 
const short ledPin1 = 11;    // the number of the LED pin
const short ledPin2 = 12;    // the number of the LED pin
const short ledPin3 = 13;    // the number of the LED pin

short dipSwitch0 ;  // variable for reading the pushbutton status
short dipSwitch1 ;
short dipSwitch2 ;
short dipSwitch3 ;
short dipSwitch4 ;
short dipSwitch5 ;
short dipSwitch6 ;
short dipSwitch7 ;
int flag=0;
int length=0;
String LEDs = "#";



void setup() {
  Serial.begin(9600); 
  pinMode(ledPin0, OUTPUT);
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin0, INPUT);
  pinMode(buttonPin1, INPUT);
  pinMode(buttonPin2, INPUT);
  pinMode(buttonPin3, INPUT);
  pinMode(buttonPin4, INPUT);
  pinMode(buttonPin5, INPUT);
  pinMode(buttonPin6, INPUT);
  pinMode(buttonPin7, INPUT);
}

void loop() {
  if (Serial.available()) { 
    if (flag == 0){
      LEDs = Serial.readString();
      LEDs.trim();
      length = LEDs.length();
      flag++;
    }
   
    // if (length == 1024){
    //     digitalWrite(ledPin0, HIGH); 
      
    // }
  }
      if (length == 1024){
        digitalWrite(ledPin0, HIGH); 
      

      dipSwitch0 = digitalRead(buttonPin0);
      dipSwitch1 = digitalRead(buttonPin1);
      dipSwitch2 = digitalRead(buttonPin2);
      dipSwitch3 = digitalRead(buttonPin3);
      dipSwitch4 = digitalRead(buttonPin4);
      dipSwitch5 = digitalRead(buttonPin5);
      dipSwitch6 = digitalRead(buttonPin6);
      dipSwitch7 = digitalRead(buttonPin7);

      int decimalInput = (dipSwitch0 << 7) | (dipSwitch1 << 6) | (dipSwitch2 << 5) | (dipSwitch3 << 4) |
                        (dipSwitch4 << 3) | (dipSwitch5 << 2) | (dipSwitch6 << 1) | (dipSwitch7 << 0);
      // Serial.print("Decimal Value: ");
      // Serial.println(decimalInput);

      int beginRow = decimalInput * 4;

      if (LEDs[beginRow] == '1') {
        digitalWrite(ledPin0, HIGH); 
      } else if (LEDs[0] == '0'){
        digitalWrite(ledPin0, LOW);
      }

      if (LEDs[beginRow + 1] == '1') { 
        digitalWrite(ledPin1, HIGH); 
      } else if (LEDs[1] == '0') {
        digitalWrite(ledPin1, LOW); 
      }

      if (LEDs[beginRow + 2] == '1') { 
        digitalWrite(ledPin2, HIGH); 
      } else if (LEDs[2] == '0'){
        digitalWrite(ledPin2, LOW); 
      }

      if (LEDs[beginRow + 3] == '1') { 
        digitalWrite(ledPin3, HIGH); 
      } else if (LEDs[3] == '0') {
        digitalWrite(ledPin3, LOW);
      }

          }


}
