open util/ordering[State]

sig State {}

abstract sig UID {}

abstract sig Source {}

sig Publication {
	source: one Source,
	uids: some UID
}

sig Author extends Source {
	authors: set State,
	profile: Publication -> State,
	visible: Publication -> State
}

sig External extends Source {}

fact multiplicity {
	all s: State {
		profile.s in Author -> set Publication
		visible.s in Author -> set Publication
	}
}

fun same[s: State, a: Author, p: Publication]: set Publication {
	let aux = a.(profile.s) <: uids | p.*(aux.~aux)
}

pred publicoes_visiveis_pertencem_ao_perfil[s: State] {
	visible.s in profile.s
}

check publicoes_visiveis_pertencem_ao_perfil {
	all s: State, a: Author, p: Publication {
		publicoes_visiveis_pertencem_ao_perfil[s] and remove_author[s, s.next, a] => publicoes_visiveis_pertencem_ao_perfil[s.next]
		publicoes_visiveis_pertencem_ao_perfil[s] and add_publication[s, s.next, a, p] => publicoes_visiveis_pertencem_ao_perfil[s.next]
	}
} for 3 but 2 State

pred source_de_publicacoes_sao_autor_ou_externa[s: State] {
    (profile.s).source in iden + (authors.s)->External
}

check source_de_publicacoes_sao_autor_ou_externa {
	all s: State, a: Author, p: Publication {
		source_de_publicacoes_sao_autor_ou_externa[s] and remove_author[s, s.next, a] => source_de_publicacoes_sao_autor_ou_externa[s.next]
		source_de_publicacoes_sao_autor_ou_externa[s] and add_publication[s, s.next, a, p] => source_de_publicacoes_sao_autor_ou_externa[s.next]
	}
} for 3 but 2 State

pred fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s: State] {
	all a: (authors.s) |
    no disj x,y: Publication |
        some x.uids & y.uids and x+y in a.profile.s and x.source = y.source
}

check fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor {
	all s: State, a: Author, p: Publication {
		fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s] and remove_author[s, s.next, a] => fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s.next]
		fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s] and add_publication[s, s.next, a, p] => fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s.next]
	}
} for 3 but 2 State

pred perfil_nao_apresenta_duas_versoes_do_mesmo_documento[s: State] {
	all a: (authors8.s) |
		no disj x,y: Publication |
			some x.uids & y.uids and x+y in a.(visible.s)
}

check perfil_nao_apresenta_duas_versoes_do_mesmo_documento {
	all s: State, a: Author, p: Publication {
		perfil_nao_apresenta_duas_versoes_do_mesmo_documento[s] and remove_author[s, s.next, a] => perfil_nao_apresenta_duas_versoes_do_mesmo_documento[s.next]
		perfil_nao_apresenta_duas_versoes_do_mesmo_documento[s] and add_publication[s, s.next, a, p] => perfil_nao_apresenta_duas_versoes_do_mesmo_documento[s.next]
	}
} for 3 but 2 State

pred remove_author[s,s': State, a: Author] {
	// Pre-condition
	a in authors.s

	// Pos-condition
	authors.s' = authors.s - a
	profile.s' = profile.s - a->Publication
	visible.s' = visible.s - a->Publication
}

pred add_publication[s,s': State, a: Author, p: Publication] {
	// Pre-conditions
	a->p not in profile.s
	a in authors.s
	p.source in a + External
	no x: Publication-p |
  	some x.uids & p.uids and x in a.(profile.s) and x.source = p.source

	// Post-conditions
	profile.s' = profile.s + a->p

	// Frame-conditions
	visible.s' = visible.s
	authors.s' = authors.s
}

run remove_author {
	some s: State, a: Author | remove_author[s, s.next, a]
} for 3 but 2 State 

run add_publication {
	some s: State, a: Author, p: Publication | add_publication[s, s.next, a, p]
} for 3 but 2 State

pred init[s: State] {
	no authors.s
}

check init_satisfies_cenas {
	all s: State {
		init[s] => fonte_apenas_adiciona_uma_versao_do_documento_ao_mesmo_autor[s]
	}
} for 3 but 1 State
