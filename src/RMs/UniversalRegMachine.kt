package RMs

// register names in the Universal Register Machine
enum class Regs{ANS, P, A, PC, N, C, R, S, T, Z}

// The following gives the required sub programs for the universal reg machine

// Assign S to R, computes S ::= R, that is copy over the contents of R to S
val l0 = BodyOp(Operation.SUB, Regs.S.ordinal, 0, 1)
val l1 = BodyOp(Operation.SUB, Regs.R.ordinal, 2, 4)
val l2 = BodyOp(Operation.ADD, Regs.Z.ordinal, 3, null)
val l3 = BodyOp(Operation.ADD, Regs.S.ordinal, 1, null)
val l4 = BodyOp(Operation.SUB, Regs.Z.ordinal, 5, 6)
val l5 = BodyOp(Operation.ADD, Regs.R.ordinal, 4, null)
val l6 = Body(Operation.HALT)



// This combines them all together to generate the universal register machine program

// These methods encode a register machine program and its input as a number