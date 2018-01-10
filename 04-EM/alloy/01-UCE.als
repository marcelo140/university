sig Aluno {}

sig Grupo {
	membros : some Aluno
}

sig Nota {}

abstract sig UCE {
    inscritos: set Aluno,
    grupos: set Grupo,
    notas: Aluno -> Nota
}

--one sig MFES, CSSI, SD, EA extends UCE {}
one sig MFES, EA extends UCE {}

fact {
	grupos in UCE one -> Grupo

	all a: Aluno | lte[#(a.~inscritos), 2]

	grupos.membros in inscritos

 	notas in inscritos->Nota
	notas in UCE -> Aluno -> lone Nota

	all u: UCE | one u.grupos & u.inscritos.~membros

	all u: UCE | lone membros.(u.notas)

	all u: UCE | membros.(u.notas) in Grupo -> lone Nota
	-- nem todos os alunos de um grupo têm nota (ainda)

}

run {} for 3 but 2 Nota
