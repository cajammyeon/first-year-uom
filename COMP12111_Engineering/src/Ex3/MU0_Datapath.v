// MU0 datapath design - structural style
// P W Nutter (based on a design by Jeff Pepper)
// Date 20/8/2021
// 

// Do not touch the following line it is required for simulation 
`timescale 1ns/100ps

// for simulation purposes, do not delete
`default_nettype none

module MU0_Datapath(
input  wire        Clk,
input  wire        Reset,
input  wire [15:0] Din,
input  wire        X_sel,
input  wire        Y_sel,
input  wire        Addr_sel,
input  wire        PC_En,
input  wire        IR_En,
input  wire        Acc_En,
input  wire [1:0]  M,
output wire [3:0]  F,			// top 4 bits of the instruction
output wire [11:0] Address,
output wire [15:0] Dout,
output wire        N,
output wire        Z,
output wire [15:0] PC,
output wire [15:0] Acc);


// Define internal signals using names from the datapath schematic
wire [15:0] X;			//output from XMux to Dout and to ALU
wire [15:0] Y;			//output from YMux
wire [15:0] IR;			//output from IR
wire [11:0] PC_In;		//output from PCReg
wire [15:0] ALU;		//output from ALU

// Instantiate Datapath components

//MU0 registers											 	
MU0_Reg12 PCReg (.Clk(Clk), .Reset(Reset), .En(PC_En),  .D(ALU[11:0]), .Q(PC_In[11:0]));   //En is PC_En, ALU is input, PC_In is output
MU0_Reg16 ACCReg(.Clk(Clk), .Reset(Reset), .En(Acc_En), .D(ALU), 	   .Q(Acc));  //En is ACC_En, ALU is input, Ac_In is output
MU0_Reg16 IRReg (.Clk(Clk), .Reset(Reset), .En(IR_En),  .D(Din), 	   .Q(IR));      //En is IR_En, Din is input, IR is output

// MU0 multiplexors					
MU0_Mux16 YMux 	  (.A(Din),    	 .B(IR), 	   			  .S(Y_sel),    .Q(Y));			//Din and IR are inputs, Y_sel is selector, Y is output
MU0_Mux16 XMux 	  (.A(Acc),      .B({4'b0000, PC[11:0]}), .S(X_sel),    .Q(X));		    //Acc_In and PC_In_Mux are inputs, X_sel is selector, X is output
MU0_Mux12 AddrMux (.A(PC[11:0]), .B(IR[11:0]),  		  .S(Addr_sel), .Q(Address));	    //PC_In and IR are inputs, Addr_sel is selector, Address is output

// MU0 ALU
MU0_Alu MU0_ALU (.X(X), .Y(Y), .M(M), .Q(ALU));	 //X is the first operand, Y is the second operand, M is the operation code, Q is the output

// MU0 Flag generation
MU0_Flags FLAGS (.Acc(Acc), .N(N), .Z(Z));	 //connect output from ALU to flag generator

// The following connects X and Dout together, there's no need for you to do so
// use X when defining your datapath structure
assign Dout = X;
// Buffer added F is op 4 bits of the instruction
assign F = IR[15:12];
assign PC = {4'b0000, PC_In};						//output to Benett is 16-bits
endmodule 

// for simulation purposes, do not delete
`default_nettype wire
