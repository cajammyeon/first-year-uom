
	B main

op1	DEFB	"Operand 1:  \0"
op2	DEFB	"Operand 2:  \0"
op3 DEFB  "Please select your operation (+, -, e) :  \0"
op4 DEFB  "Result of Operation :  \0"

  ALIGN

plus   EQU 43
minus  EQU 45
return EQU 10
exit   EQU 101

main

  ADR	R0, op1	   ; printf("Operand 1: ")
  SVC 3
  SVC	5		       ; input a digit to R0
  MOV R1, R0     ; store R0 into R1

  ADR	R0, op2	   ; printf("Operand 2: ")
  SVC 3
  SVC	5		       ; input a digit to R0
  MOV R2, R0     ; store R0 into R2

	ADR R0, op3    ; printf("Please select your operation : ")
	SVC 3
  SVC 1          ; input to R0           
  MOV R3, R0     ; store R0 into R3
  SVC 0          ; print the operation
  ADR R0, return ; simulating return key
  SVC 0

  CMP R3, #plus
  BEQ plusfunction
  CMP R3, #minus
  BEQ minusfunction
  CMP R3, #exit
  BEQ exitfunction
  B main

plusfunction
  ADR R0, op4    ; printf("Result of operation : ")
  SVC 3
  ADD R0, R1, R2
  SVC 4          ; printf(operand 1 + operand 2)
  ADR R0, return ; simulating return key
  SVC 0
  B main

minusfunction
  ADR R0, op4    ; printf("Result of operation : ")
  SVC 3
  SUB R0, R1, R2
  SVC 4          ; printf(operand 1 - operand 2)
  ADR R0, return ; simulating return key
  SVC 0
  B main

exitfunction
  SVC 2		     ; stop the program
