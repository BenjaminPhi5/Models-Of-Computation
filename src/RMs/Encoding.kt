package RMs

import kotlin.math.log2
import kotlin.math.roundToLong

// This provides the methods to encode a program as a number, and a list of register start states as a program

// ENCODING

fun __xpairy__(x : Long, y : Long) : Long {
    var z = 2L
    if (x == 0L) z = 1
    else {
        for (i in 2 until x + 1)
            z = z shl 1
    }

    return z * (2*y+1)
}

fun _xpairy_(x : Long, y : Long) : Long {
    return __xpairy__(x,y) - 1
}

fun testpairs(){
    println(__xpairy__(2,2))
    println(_xpairy_(0,2))
    println(_xpairy_(0,0))
    println(_xpairy_(0,1))
    println(_xpairy_(1,1))
}

fun encodeList(a : Array<Long>, index : Int) : Long {

    if(index == a.size){
        return 0
    } else return __xpairy__(a[index], encodeList(a, index+1))

}

fun testList(){
    println(encodeList(arrayOf(2,1,3), 0))
}

fun encodeBody(b : Body) : Long {
    if (b.op == Operation.HALT || b.op == Operation.EXIT || b.op == Operation.GOTO) {
        return 0 //there wont actually be any gotos or exits in real reg machine programs, only in my universal machine
    }
    else{
        val opBody = b as BodyOp
        when(opBody.op) {
            Operation.ADD -> return __xpairy__(2L* opBody.reg, 1L * opBody.l1)
            Operation.SUB -> return __xpairy__(2 * opBody.reg + 1L, _xpairy_(1L*opBody.l1, 1L*opBody.l2!!))
        }
    }
    println("shouldnt have got to here:")
    return 0
}

fun encodeProgram(p : Array<Body>) : Long {
    var bList : Array<Long> = LongArray(p.size).toTypedArray()
    for(i in 0 until p.size)
        bList[i] = encodeBody(p[i])

    return encodeList(bList, 0)
}

fun testEncodeBody(){
    val l0 = BodyOp(Operation.SUB, 0, 0, 2)
    println(encodeBody(l0))
}

fun testEncodeProgram(){
    val l0 = BodyOp(Operation.SUB, 0, 0, 2)
    val l1 = Body(Operation.HALT)
    println(encodeProgram(arrayOf(l0, l1)))
}

// DECODING Allows for decoding of pairs, lists, bodies, programs

fun decode__(l : Long) : Array<Long> {
    var x = Math.floor(log2(0.0+l))
    val y = ((l / (Math.pow(2.0, x))) - 1) / 2
    println("x: $x")
    println("y: $y")
    println("l: $l")
    return arrayOf(x.roundToLong(),y.roundToLong())
}

fun decode_(l:Long) : Array<Long> {
    return decode__(l+1)
}

fun decodeList_(l : Long) : ArrayList<Long> {
    var a : ArrayList<Long> = ArrayList()
    if(l != 0L) {
        val pair = decode__(l)
        a.add(pair[0])
        if (pair[0] != 0L) a.addAll(decodeList_(pair[1]))
    }
    return a

}

fun decodeList(l : Long) : Array<Long> {
    val a = decodeList_(l)
    return a.toArray(arrayOfNulls<Long>(a.size))
}

fun decodeBody(l : Long) : Body {
    if(l == 0L)
        return Body(Operation.HALT)
    else {
        val x = decode__(l)

        if(x[0] %2 == 0L){
            return BodyOp(Operation.ADD, (x[0]/2).toInt(), x[1].toInt(), null)
        } else {
            val z = decode_(x[1])
            return BodyOp(Operation.SUB, (((x[0])-1)/2).toInt(), z[0].toInt(), z[1].toInt())
        }
    }
}

fun decodeProgram(l : Long) : Array<Body> {
    val ns = decodeList_(l)
    var prog = ArrayList<Body>()
    for(b in ns){
        prog.add(decodeBody(b))
    }

    return prog.toArray(arrayOfNulls<Body>(prog.size))
}