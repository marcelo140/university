-module(lock).
-export([create/0, acquire/2, release/1]).

create() ->
    spawn(fun released end)).

acquire(Pid, Mode) when Mode == read; Mode == write ->
    Pid ! {Mode, self()},
    receive acquired -> true end.

release(Pid) ->
    Pid ! {release, self()}.

released() ->
    receive
        {read, Pid} -> Pid ! acquired, reading([Pid], 5);
        {write, Pid} -> Pid ! acquired, writing(Pid)
    end.

reading([], _) -> released();
reading(Readers, 1) -> 
    receive 
        {Release, Pid} -> reading(Readers -- [Pid], 1)
    end.
reading(Readers, Slots) ->
    receive
        {read, Pid} -> Pid ! acquired, reading([Pid | Readers], Slots - 1);
        {release, Pid} -> reading(Readers -- [Pid], Slots)
    end.

writing(Pid) ->
    receive
        {release, Pid} -> released()
    end.
