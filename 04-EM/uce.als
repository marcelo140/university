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

one sig MFES, CSSI, SD, EA extends UCE {}

fact {
	lte[#(Aluno.~inscritos), 2]
  --all a: Aluno | lte[#(a.~inscritos), 2]

	all u: UCE | u.grupos.membros in u.inscritos

  all u: UCE | lone Aluno.(u.notas)
	--all u: UCE, a: u.inscritos | lone a.(u.notas)
	all u : UCE | u.notas.Nota in u.inscritos

	all u : UCE, a : u.inscritos | one u.grupos & a.~membros

	all g: Grupo, n: Nota | some a: g.membros | a->n in g.~grupos.notas implies g.membros->n in g.~grupos.notas



}

run {}
