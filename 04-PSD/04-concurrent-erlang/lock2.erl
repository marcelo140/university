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
        {read, Pid} -> Pid ! acquired, reading([Pid]);
        {write, Pid} -> Pid ! acquired, writing(Pid)
    end.

reading([], _) -> released();
reading(Readers) ->
    receive
        {read, Pid} -> Pid ! acquired, reading([Pid | Readers]);
        {write, Pid} -> finishReading(Readers, Pid);
        {release, Pid} -> reading(Readers -- [Pid])
    end.

finishReading([], Writer) -> writing(Writer).
finishReading(Readers, Writer) ->
    receive
        {release, Pid} -> reading(Readers -- [Pid])
    end.

writing(Pid) ->
    receive
        {release, Pid} -> released()
    end.

