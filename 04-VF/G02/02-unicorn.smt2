(set-logic QF_UF)
(declare-sort Animal 0)

(declare-fun mythical (Animal) Bool)
(declare-fun immortal (Animal) Bool)
(declare-fun mammal (Animal) Bool)
(declare-fun horned (Animal) Bool)
(declare-fun magical (Animal) Bool)

(declare-fun unicorn () Animal)

(assert (=> (mythical unicorn) (immortal unicorn)))
(assert (=> (not (mythical unicorn)) (and (not (immortal unicorn)) (mammal unicorn))))
(assert (=> (or (immortal unicorn) (mammal unicorn)) (horned unicorn)))
(assert (=> (horned unicorn) (magical unicorn)))

(check-sat)
(get-model)
