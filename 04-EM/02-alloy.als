open util/ordering[Hour]
open util/ordering[State]

sig State {}

sig Number {}

sig Hour {}

one sig Actual in Hour {}

sig Name {
	contacts: Number -> State
}

sig Call {
	number: Number -> State,
	hour: Hour -> State
}

fact arity {
	all s: State {
		all c: Call | one c.hour.s
		all c: Call | one c.number.s
		all n: Name | some n.contacts.s
	}
}

fact system {
	all s: State {
      -- Um número não pode pertencer a duas pessoas diferentes
	    all n: Number | lone n.~(contacts.s)

	    -- Todos os números chamados fazem parte dos contacts
	    Call.number in Name.contacts

	    -- Não podem existir chamadas simultâneas
	    (hour.s).~(hour.s) in iden

	    -- Todas as chamadas ocorrem antes da hora atual
	    Call.(hour.s).lt[Actual]
	}
}

pred novo[s, s': State, na: Name, nu: Number] {
	no na.(contacts.s) & nu

	contacts.s' = contacts.s + na->nu

	hour.s' = hour.s
	number.s' = number.s
}

pred apaga[s, s': State, na: Name]{
	contacts.s' = contacts.s - na->Number

	hour.s' = hour.s
	number.s' = number.s
}


run novo_contacto_e_consistente {
	some s: State, name: Name, number: Number | novo[s, s.next, name, number]
} for 3 but 2 State

run apaga_contacto_e_consistente {
	some s: State, name: Name | apaga[s, s.next, name]
} for 3 but 2 State

run {} for 3
