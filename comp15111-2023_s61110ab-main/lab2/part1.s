
	B main

op1	DEFB	"Operand 1:  \0"
op2	DEFB	"Operand 2:  \0"
op3 DEFB  "Result of Addition : \0"

	ALIGN

main

  ADR	R0, op1	; printf("Operand 1: ")
  SVC 3
  SVC	5		    ; input a digit to R0
  MOV R1, R0  ; store R0 into R1

  ADR	R0, op2	; printf("Operand 2: ")
  SVC 3
  SVC	5		    ; input a digit to R0
  MOV R2, R0  ; store R0 into R2

    ADR R0, op3 ; printf("Result of Addition: )
    SVC 3

  ADD R0, R1, R2
  SVC 4       ; printf(operand 1 + operand 2)

  SVC 2		    ; stop the program
