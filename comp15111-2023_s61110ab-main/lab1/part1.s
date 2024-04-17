	LDR	R0, x
	LDR	R1, y
	LDR	R2, c
	LDR	R3, d
	LDR R4, zero
    CMP R1, R4
	BEQ end
	MOV R3, R1
	MOV R5, R0
loop
	MUL R5, R5, R0
	STR R5, c
	SUB R3, R3, #1
	CMP R3, #1
	BEQ end
	B loop
end
	SVC 2
x	DEFW	2
y   DEFW  10
c	DEFW	1
d	DEFW	0
zero DEFW 0
