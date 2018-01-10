open util/ordering[State]

sig State {}

abstract sig Being {
	eats: set Being,
	where: Bank one -> State
}

one sig Farmer, Wolf, Sheep, Beans extends Being {}

fact Eats {
	eats = Wolf->Sheep + Sheep->Beans
}

abstract sig Bank {
	cross: one Bank
}

one sig Left, Right extends Bank {}

fact Cross {
	cross = Left->Right + Right->Left
}

pred init[s: State] {
	Being = (where.s).Left
}

pred alone[s,s': State] {
	-- Pre conditions
	no x,y: (where.s).(Farmer.(where.s))-Farmer | x in y.eats

	-- Post conditions
	Farmer.(where.s') = Farmer.(where.s).cross

	all b: Being-Farmer | b.(where.s') = b.(where.s)
}

pred notalone[b: Being, s,s': State] {
	-- Pre-conditions
	b != Farmer
	b.(where.s) = Farmer.(where.s)
	no x,y: (where.s).(Farmer.(where.s))-(Farmer+b) | x in y.eats

	-- Post-conditions
	Farmer.(where.s') = Farmer.(where.s).cross
	b.(where.s') = b.(where.s).cross

	-- Frame-conditions
	all x: Being-(Farmer+b) | x.(where.s') = x.(where.s)
}

pred noeating[s: State] {
	all b: Bank {
		Farmer.(where.s) = b
		or
		no x,y: (where.s).b | x in y.eats
	}
}

pred notright[s: State] {
	Being not in (where.s).Right
}

pred allright[s: State] {
	Being in (where.s).Right
}

check init_satisfies_noeating {
	all s: State |
		init[s] implies noeating[s]
}	for 3 but 1 State

check alone_preserves_noeating {
	all s,s': State |
		noeating[s] and alone[s,s'] implies noeating[s']
}	for 3 but 2 State

check notalone_preserves_noeating {
	all s,s': State, b: Being |
		noeating[s] and notalone[b, s,s'] implies noeating[s']
} for 3 but 2 State

fact valid_path_prefixes {
	init[first]
	all s: State-last {
		alone[s, s.next]
		or
		some b: Being | notalone[b, s, s.next]
	}
}

check puzzle_cannot_be_solved {
	all s: State | notright[s]
}	for 3 but 8 State

pred equal[s,s': State] {
	where.s = where.s'
}

pred loop {
	some disj s,s': State | equal[s,s']
}

check puzzle_will_always_be_solved {
	loop implies (some s: State | allright[s])
} 

run {}
