// MU0 ALU design 
// P W Nutter (based on a design by Jeff Pepper)
// Date 7/7/2021

// Do not touch the following line it is required for simulation 
`timescale 1ns/100ps
`default_nettype none

// module header

module MU0_Alu (
               input  wire [15:0]  X, 
               input  wire [15:0]  Y, 
               input  wire [1:0]   M, 
               output reg  [15:0]  Q
	       );

// behavioural description for the ALU

reg [15:0] YI; //YI is for inverse of Y

always @ (*) begin
YI = ~Y ; //inverting Y as Y inverse
case(M)
	2'b00 : Q = Y ; 			//pass through 
	2'b01 : Q = X + Y ; 		//addition
	2'b10 : Q = X + 1 ; 		//increment by 1
	2'b11 : Q = X + YI + 1 ; 	//subtraction through 2's complement
	default : Q = 16'hxxxx ;	//default case reset to x
endcase end
endmodule 

// for simulation purposes, do not delete
`default_nettype wire
