package RMs

// NOTE goto doesn't technically exist in the reg machine Abstraction, its just easier to use when combining register
// machines, as you don't have to go and update each label that points to an exit or halt inside a sub machine, just
// replace each halt and exit with a goto instead. Semantically it achieves the same thing and so is a valid register
// machine operation.
enum class Operation {ADD, SUB, HALT, EXIT, GOTO}

open class Body(var op: Operation)

class BodyOp(op: Operation, val reg: Int, var l1: Int, var l2: Int?) : Body(op)

class RegisterMachine(private var prog: Array<Body>, private var init: Array<Long>, private val doInit : Boolean){

    val regs : HashMap<Int, Long> = HashMap()

    // Need to have a method to run the input to this register machine now, and store the output.
    // Will use the convention the the answer is the value stored in R0 at the end
    fun run() {
        // setup R0 with 0
        regs[0] = 0

        //if the program empty, exit:
        if(prog.isEmpty()) return

        // copy the starting values into the relevant registers, if doInit is true
        if(doInit) { // this may not be true for parts of the universal register machine or any other combined machine
            for (i in 0 until init.size)
                regs[i] = init[i]
        }

        // run the program -  at this point, if it doesn't terminate quickly, there is no control to halt it
        var current : Body = prog[0]
        var opBody : Body

        while(current.op != Operation.HALT && current.op != Operation.EXIT){

            opBody = current as BodyOp // get body contents out

            // if its a goto alias in a combined reg machine, jump to the relevant register
            if(current.op == Operation.GOTO)
                current = prog[opBody.l1]
            else {

                val regContent: Long = regs.getOrDefault(opBody.reg, 0)

                // If an add, increment the relevant register value, go to the next label
                if (current.op == Operation.ADD) {
                    regs[opBody.reg] = regContent + 1
                    current = prog[opBody.l1]

                    // If a sub, check for 0, if 0 then go to l1, else sub and go to l2.
                } else {
                    if (regContent == 0L)
                        current = prog[opBody.l2 as Int]
                    else {
                        regs[opBody.reg] = regContent - 1
                        current = prog[opBody.l1]
                    }
                }
            }
        }
    }
}

// Prints out a register machine program in a human readable format such as
/*
* l0 : R0+ -> l1
  l1 : R1+ -> l2
  l2 : R2+ -> l3
  l3 : GOTO l4
  l4 : R0+ -> l1
  l5 : R1+ -> l2
  l6 : R2+ -> l3
  l7 : HALT
* */
fun printProgram(prog : Array<Body>) {

    var opBody : BodyOp

    for(i in 0 until prog.size){
        if(prog[i].op == Operation.HALT || prog[i].op == Operation.EXIT)
            println("l$i : ${prog[i].op}")

        else {
            opBody = prog[i] as BodyOp
            when(opBody.op){
                Operation.ADD -> println("l$i : R${opBody.reg}+ -> l${opBody.l1}")
                Operation.GOTO -> println("l$i : ${opBody.op} l${opBody.l1}")
                Operation.SUB -> println("l$i : R${opBody.reg}- -> l${opBody.l1}, l${opBody.l2}")
            }
        }

    }

}