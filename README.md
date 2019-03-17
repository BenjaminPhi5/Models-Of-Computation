<h2 align="center">
  <br>
  Models of Computation
  <br>
</h2>

This is a simple implementation of register machines, primitive recursive functions and the lambda calculus using Kotlin.
Each section contains code to contruct and compute the models along with a few examples of them in use.

> This project is in an incompleted state, as it stands you have to build up the AST for all components manually,
> I intend to add functionality to input code in a human readable way later on

The register machine also comes with the Universal Register Machine, built up by combining register machine together. The implementation has no practical use due to a very inefficient encoding of programs as data.

#### Lambda Calculus
This takes variables: ```x, y, z, ...```, abstraction ```(λ x. M)```, where M is a lambda, and application ```M N```.
Lambdas such as ```(λ x1 f y. f (x1 f y))``` can be built up from sucessive constructions of variables, abstractions and lambdas.

There are functions to continually reduce an expression, either until it cannot be reduced any more or until a specified cap on reduction steps has been reached.

There is also functionality to generate and test for Chuch numerals: ```(λ f x. f^n x)```

#### Primitive recursive functions
The code contains contructors for the basis functions: ```succ(x) = x + 1; zero^n(x1,...,xn) = 0; projnj(x1,...xn) = xj```
along with composition: ```f ∘ [g1,...gm]```, primitive recursion ```ρ(base,rec)``` and minimisation ```μ(f)```.
There is also fucntionality to compute the result of any contructed function. A prettyprinter for this section has not yet been added

#### Register machines
The code contains the ability to contruct register machine programs, with the following format:
```[label] : [RegName]+ -> [nextlab1Name]``` or ```[label] : [RegName]- -> [nextLab1Name], [nextLab2Name]``` eg:
```
l0 : R1- -> l1, l2
l1 : R0+ -> l0
l2 : R2- -> l3, l4
l3 : R0+ -> l2
l4 : HALT
```

Register machines can also be combined, and to acheieve flow control more nicely, the instructions EXIT and GOTO have been added (EXIT gives an alternate finishing state and GOTO allows combining machines together a bit easier, both of these are just simple abstractions on top of the standard register machine language).


The code for the universal register machine by combining all its components together is (although there may be a small bug I haven't properly tested):
```l0 : R9+ -> l1
l1 : R2- -> l2, l3
l2 : R9+ -> l0
l3 : R9- -> l4, l5
l4 : R2+ -> l3
l5 : R0- -> l1, l6
l6 : GOTO l7
l7 : R8- -> l7, l8
l8 : R1- -> l9, l11
l9 : R9+ -> l10
l10 : R8+ -> l8
l11 : R9- -> l12, l13
l12 : R1+ -> l11
l13 : GOTO l14
l14 : R4- -> l14, l15
l15 : R8- -> l17, l16
l16 : GOTO l25
l17 : R8+ -> l18
l18 : R8- -> l19, l20
l19 : R9+ -> l18
l20 : R9- -> l22, l21
l21 : R4+ -> l18
l22 : R9- -> l23, l24
l23 : R8+ -> l20
l24 : GOTO l36
l25 : R0- -> l25, l26
l26 : R2- -> l28, l27
l27 : HALT
l28 : R2+ -> l29
l29 : R2- -> l30, l31
l30 : R9+ -> l29
l31 : R9- -> l33, l32
l32 : R0+ -> l29
l33 : R9- -> l34, l35
l34 : R2+ -> l31
l35 : HALT
l36 : R3- -> l37, l38
l37 : GOTO l14
l38 : GOTO l39
l39 : R5- -> l39, l40
l40 : R4- -> l42, l41
l41 : GOTO l25
l42 : R4+ -> l43
l43 : R4- -> l44, l45
l44 : R9+ -> l43
l45 : R9- -> l47, l46
l46 : R5+ -> l43
l47 : R9- -> l48, l49
l48 : R4+ -> l45
l49 : GOTO l50
l50 : R6- -> l50, l51
l51 : R2- -> l53, l52
l52 : GOTO l61
l53 : R2+ -> l54
l54 : R2- -> l55, l56
l55 : R9+ -> l54
l56 : R9- -> l58, l57
l57 : R6+ -> l54
l58 : R9- -> l59, l60
l59 : R2+ -> l56
l60 : GOTO l61
l61 : R5- -> l62, l63
l62 : GOTO l64
l63 : GOTO l74
l64 : R5- -> l65, l66
l65 : GOTO l67
l66 : GOTO l90
l67 : R9+ -> l68
l68 : R7- -> l69, l70
l69 : R9+ -> l67
l70 : R9- -> l71, l72
l71 : R7+ -> l70
l72 : R6- -> l68, l73
l73 : GOTO l50
l74 : R6+ -> l75
l75 : GOTO l76
l76 : R3- -> l76, l77
l77 : R4- -> l78, l80
l78 : R9+ -> l79
l79 : R3+ -> l77
l80 : R9- -> l81, l82
l81 : R4+ -> l80
l82 : GOTO l83
l83 : R9+ -> l84
l84 : R2- -> l85, l86
l85 : R9+ -> l83
l86 : R9- -> l87, l88
l87 : R2+ -> l86
l88 : R6- -> l84, l89
l89 : GOTO l106
l90 : R4+ -> l91
l91 : GOTO l92
l92 : R3- -> l92, l93
l93 : R4- -> l95, l94
l94 : GOTO l103
l95 : R4+ -> l96
l96 : R4- -> l97, l98
l97 : R9+ -> l96
l98 : R9- -> l100, l99
l99 : R3+ -> l96
l100 : R9- -> l101, l102
l101 : R4+ -> l98
l102 : GOTO l103
l103 : R6- -> l104, l105
l104 : GOTO l83
l105 : GOTO l76
l106 : R6- -> l106, l107
l107 : R7- -> l109, l108
l108 : GOTO l7
l109 : R7+ -> l110
l110 : R7- -> l111, l112
l111 : R9+ -> l110
l112 : R9- -> l114, l113
l113 : R6+ -> l110
l114 : R9- -> l115, l116
l115 : R7+ -> l112
l116 : GOTO l83
