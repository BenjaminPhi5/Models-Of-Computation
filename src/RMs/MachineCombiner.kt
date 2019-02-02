package RMs

// this takes a list of machines with exit and halts and replaces them with a goto label
// it will do this at runtime, to make my universal register machine out of register machine components

// need an enum for the different parts of the universal register machine
// need a function that takes in a default program, but replaces certain registers with others etc noice :)

// just use a list of register machines, and run all of them.

// This is a container for a program, with a list of alias registers, to allow one program to be used multiple times,
// operating on different registers
class Module(var prog: Array<Body>, val aliases : HashMap<Int, Int>){


    fun mapAliases(){

        if(!aliases.isEmpty()){

            // first, makes a new prog to put the new reg code bodies in.
            var progNew : ArrayList<Body> = ArrayList()
            var b : Body
            var bOp : BodyOp

            for(i in 0 until prog.size){
                b = prog[i]

                if(b.op != Operation.HALT && b.op != Operation.EXIT){
                    bOp = b as BodyOp

                    if(bOp.reg in aliases)
                        progNew.add(BodyOp(b.op, aliases[b.reg]!!, b.l1, b.l2))
                        // the whole !! thing is probably not the correct way to do it,
                        // it converts the value to a type that cant be null and returns null if it is, I think
                        // need to read more about nulls in kotlin and correct use of !!
                    else
                        progNew.add(BodyOp(b.op, b.reg, b.l1, b.l2))
                }

                else{
                    progNew.add(Body(b.op))
                }

            }

            prog = progNew.toArray(arrayOfNulls(progNew.size))
        }

    }

}

class combiner(var modules : Array<Module>, val haltMap : HashMap<Module, Module>,
               val exitMap: HashMap<Module, Module>){

    // This will take a group of modules and combine them together to make one program
    // It will increment registers, and it will replace Halts and Exits with Gotos as approporiate

    var progIndexes : HashMap<Module, Int> = HashMap()

    // Calculates the index into the final program for each module
    fun setupIndexes(){

        if(!modules.isEmpty())
            progIndexes[modules[0]] = 0

        for(i in 1 until modules.size)
            // again, a dangerous use of the !! again, assuming well formed map so far and all that
            // I wonder what the correct way of doing this really is.
            progIndexes[modules[i]] = progIndexes[modules[i-1]]!! + modules[i-1].prog.size

    }

    fun combine() : Array<Body> {
        // First, map all the aliases.
        for(m in modules)
            m.mapAliases()

        // Then, get indexes for each module in the final program
        setupIndexes()
        println(progIndexes)
        println(progIndexes.size)

        // Now, build up the new prog
        var finalProg : ArrayList<Body> = ArrayList()
        var moduleIndex : Int
        var opBody : BodyOp

        for(m in modules){
            //println("module $m")
            for(b in m.prog){
                //println("next body")

                if(b.op != Operation.EXIT && b.op != Operation.HALT){
                    opBody = b as BodyOp
                    when(opBody.op){
                        Operation.ADD -> finalProg.add(BodyOp(Operation.ADD, opBody.reg,
                                opBody.l1+progIndexes[m]!!, null))
                        Operation.SUB -> finalProg.add(BodyOp(Operation.SUB, opBody.reg,
                                opBody.l1+progIndexes[m]!!, opBody.l2!!+progIndexes[m]!!))
                        Operation.GOTO -> finalProg.add(BodyOp(Operation.ADD, 0,
                                opBody.l1+progIndexes[m]!!, null))
                    }

                } else {
                    // replace Halts and Exits with Gotos, unless there's no mapping, in which case, Halt
                    try {
                        if (b.op == Operation.HALT) {
                            if (m !in haltMap)
                                finalProg.add(Body(Operation.HALT))
                            else {
                                // goto the mapping for the linked module in module index.
                                moduleIndex = progIndexes[haltMap[m]]!!
                                finalProg.add(BodyOp(Operation.GOTO, 0, moduleIndex, null))
                            }
                        } else {
                            if (m !in exitMap)
                                finalProg.add(Body(Operation.HALT))
                            else {
                                moduleIndex = progIndexes[exitMap[m]]!!
                                finalProg.add(BodyOp(Operation.GOTO, 0, moduleIndex, null))

                            }
                        }
                    } catch (e: NullPointerException) {
                        error("halting or exiting module not in module list")
                    }
                }
            }
        }

        val progArray = arrayOfNulls<Body>(finalProg.size)
        //println("returning")
        return finalProg.toArray(progArray)
    }

}