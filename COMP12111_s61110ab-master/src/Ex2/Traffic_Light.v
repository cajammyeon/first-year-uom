// COMP12111 - Exercise 2 Sequential Design
//
// Version 2. April 2023. P W Nutter
//
// This is the Verilog module for the pedestrian/cyclist crossing Controller
//
// The aim of this exercise is complete the finite state machine using the
// state transition diagram given in the laboratory notes. 
//
// DO NOT change the interface to this design or it may not be marked completely
// when submitted.
//
// Make sure you document your code and marks may be awarded/lost for the 
// quality of the comments given.
//
// Add your comments:
//
//

`timescale  1ns / 100ps
`default_nettype none

module Traffic_Light ( output reg [4:0] 	lightseq,	//the 5-bit light sequence
		           input  wire              clock,		//clock that drives the fsm
		           input  wire              reset,		//reset signal 
		           input  wire              start);		//input from pedestrian


// declare internal variables here (how many bits required?)
reg [3:0] current_state, next_state;

//localparam state0 = 4'b0000;
//localparam state1 = 4'b0001;
//localparam state2 = 4'b0010;
//localparam state3 = 4'b0011;
//localparam state4 = 4'b0100;
//localparam state5 = 4'b0101;
//localparam state6 = 4'b0110;
//localparam state7 = 4'b0111;
//localparam state8 = 4'b1000;
//localparam state9 = 4'b1001;
//localparam state10 = 4'b1010;

// implement your next state combinatorial logic block here
always @ (current_state, start)
case(current_state)
	4'b0000: if (start) next_state = 4'b0001;
			 else next_state = 4'b0000;
	4'b0001: next_state = 4'b0010;
	4'b0010: next_state = 4'b0011;
	4'b0011: next_state = 4'b0100;
	4'b0100: next_state = 4'b0101;
	4'b0101: if (start) next_state = 4'b1000;
			 else next_state = 4'b0110;
	4'b0110: if (start) next_state = 4'b1001;
			 else next_state = 4'b0111;
	4'b0111: if (start) next_state = 4'b1010;
			 else next_state = 4'b0000;
	4'b1001: next_state = 4'b1010;
	4'b1010: next_state = 4'b0001;
endcase 

// implement your current state assignment, register, here

always @ (posedge clock, posedge reset)
if (reset) current_state <= 4'b0000;
else current_state <= next_state;

// implement your output logic here

always @ (current_state)
case (current_state)
	4'b0000, 4'b0110, 4'b0111, 4'b1000, 4'b1001, 4'b1010 : lightseq = 5'b0_1001;
	4'b0001 : lightseq = 5'b0_1010;
	4'b0010, 4'b0011, 4'b0100 : lightseq = 5'b1_0100;
	4'b0101 : lightseq = 5'b0_1110;
endcase
		
endmodule

`default_nettype wire
