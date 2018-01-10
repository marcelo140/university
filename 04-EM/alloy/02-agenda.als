open util/ordering[Hora]
open util/ordering[State]

sig State {}

sig Numero {}

sig Hora {}

sig Atual in Hora {
	atuais: set State
}

sig Nome {
	nomes: set State,
	contactos: Numero -> State
}

sig Chamada {
	chamadas: set State,
	numero: one Numero,
	hora: one Hora
}

/*
fact multiplicidade {
	atuais in Atual one -> State

	all s: State {
		contactos.s in Nome lone -> Numero
	}
}
*/

/////////////////////////// OPERAÇÕES //////////////////////////////////


abstract sig Evento {
	pre, pos: one State
}{
}

sig Nop extends Evento {
}{
}

sig Add extends Evento {
	nome: one Nome,
	num: one Numero
}{
	acrescenta_numero[pre, pos, nome, num]
}

sig Delete extends Evento {
	nome: one Nome
}{
	apagar_nome[pre, pos, nome]
}

sig Call extends Evento {
	numero: one Numero
}{
	efetuar_chamada[pre, pos, numero]
}

pred avanca_relogio[s,s': State] {
--	some (atuais.s).next
--	one (atuais.s)

	// Post-conditions
	atuais.s' = (atuais.s).next

	// Frame-conditions
	// nomes.s' = nomes.s
	// chamadas.s' = chamadas.s
	// contactos.s' = contactos.s
}

pred acrescenta_numero[s,s': State, n: Nome, num: Numero] {
	// Pre-conditions
	no (contactos.s).num

	// Post-conditions
	avanca_relogio[s, s']
	contactos.s' = contactos.s + n->num
	nomes.s' = nomes.s + n

	// Frame-conditions
	chamadas.s' = chamadas.s
}

pred apagar_nome[s,s': State, n: Nome] {
	// Pre-conditions
	n in nomes.s

	// Post-conditions
	avanca_relogio[s, s']
	contactos.s' = contactos.s - n->Numero
	nomes.s' = nomes.s - n

	// Frame-conditions
	all x: Nome-n | x.(contactos.s') = x.(contactos.s)
	chamadas.s' = chamadas.s
}

pred efetuar_chamada[s,s': State, n: Numero] {
	n in (nomes.s).(contactos.s)
	numero_unico[s]
	
	// Pos-condition
	avanca_relogio[s, s']
	some c: Chamada - chamadas.s {
		chamadas.s' = chamadas.s + c
		c.numero = n
		c.hora = atuais.s'
	}

	contactos.s' = contactos.s
	nomes.s' = nomes.s
}

/////////////////////////////// NÚMERO ÚNICO ////////////////////////////////////////

/*
fact numero_unico {
	all s: State |
		(contactos.s).~(contactos.s) in iden
}
*/

pred numero_unico[s: State] {
	(contactos.s).~(contactos.s) in iden
}

check operacoes_preservam_numero_unico {
	all s: State, n: Nome, num: Numero {
		numero_unico[s] and acrescenta_numero[s, s.next, n, num] implies numero_unico[s.next]
		numero_unico[s] and apagar_nome[s, s.next, n] implies numero_unico[s.next]
		numero_unico[s] and efetuar_chamada[s, s.next, num] implies numero_unico[s.next]
	}
} for 3 but 2 State, 0 Evento

///////////////////////////// CHAMADAS SIMULTÂNEAS ///////////////////////////////

/*
fact nao_existem_chamadas_simultaneas {
	hora.~hora in iden
}
*/

pred chamadas_simultaneas[s: State] {
	hora in (chamadas.s) lone -> (atuais.s + (atuais.s).prevs)
}

check operacoes_preservam_chamadas_simultaneas {
	all s: State, n: Nome, num: Numero {
		chamadas_simultaneas[s] and acrescenta_numero[s, s.next, n, num] implies chamadas_simultaneas[s.next]
		chamadas_simultaneas[s] and apagar_nome[s, s.next, n] implies chamadas_simultaneas[s.next]
		chamadas_simultaneas[s] and efetuar_chamada[s, s.next, num] implies chamadas_simultaneas[s.next]
	}
} for 3 but 2 State, 0 Evento

//////////////////////////// NUMEROS NOS CONTACTOS ////////////////////////////

/*
fact numeros_chamados_fazem_parte_dos_contactos {
	all s: State |
		--Chamada.numero in Nome.(contactos.s)
		Chamada.numero in (nomes.s).(contactos.s)
}*/

pred numero_nos_contactos[s: State] {
	(chamadas.s).numero in (nomes.s).(contactos.s)
}

check operacoes_preservam_numero_nos_contactos {
	all s: State, n: Nome, num: Numero {
		numero_nos_contactos[s] and acrescenta_numero[s, s.next, n, num] implies numero_nos_contactos[s.next]
		--numero_nos_contactos[s] and apagar_nome[s, s.next, n] implies numero_nos_contactos[s.next]
		numero_nos_contactos[s] and efetuar_chamada[s, s.next, num] implies numero_nos_contactos[s.next]
	}
} for 3 but 2 State, 0 Evento

////////////////////////////// TESTEAR PREDICADOS ///////////////////////////////

run avanca_relogio_e_consistente {
	some s: State | avanca_relogio[s, s.next]
} for 0 but 2 State, 3 Hora, 0 Evento

run acrescenta_numeros_e_consistente {
	some s: State, n: Nome, num: Numero | acrescenta_numero[s, s.next, n, num]
} for 3 but 0 Chamada, 2 State, 0 Evento

run apagar_numeros_e_consistente {
	some s: State, n: Nome | apagar_nome[s, s.next, n]
}	for 3 but 0 Chamada, 2 State, 0 Evento

run efetuar_chamada_e_consistente {
	some s: State, n: Numero | efetuar_chamada[s, s.next, n]
} for 3 but 2 State, 0 Evento


pred tracos {
	no chamadas.first
	no nomes.first
	no contactos.first

	all s: State-last | one e: Evento {
		e.pre = s and e.pos = s.next

		chamadas.s != chamadas.(s.next) implies e in Call
		contactos.s.next != contactos.s and contactos.(s.next) in contactos.s implies e in Delete
		contactos.s.next != contactos.s and contactos.s in contactos.(s.next) implies e in Add

	}
}

run { tracos and all s: State - last | pre.s not in Nop } for 5 but exactly 6 State, 7 Hora, 6 Evento, exactly 1 Call
