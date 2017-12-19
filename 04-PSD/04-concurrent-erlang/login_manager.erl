-module(login_manager).
-export([start/0, create_account/2, close_account/2, login/2, logout/1, online/0]).

start() -> register(?MODULE, spawn(fun () -> loop(#{}) end)).

loop(Acc) ->
    receive
        { create, From , Username, Passwd } ->
            case maps:is_key(Username, Acc) of
                false ->
                    From ! {?MODULE, ok },
                    loop(maps:put(Username, { Passwd, true }, Acc));
                true -> From ! {?MODULE, user_exists},
                        loop(Acc)
            end;
        { close, From, Username, Passwd } ->
            case maps:find(Username, Acc) of
                {ok, {Passwd, _ }} -> From ! {?MODULE, ok},
                                      loop(maps:remove(Username, Acc));
                _ -> From ! {?MODULE, invalid},
                     loop(Acc)
            end;
        { login, From, Username, Passwd } ->
            case maps:find(Username, Acc) of
                {ok, {Passwd, _ }} -> From ! {?MODULE, ok},
                                      loop(maps:update(Username, { Passwd, true}, Acc));
                _ -> From ! {?MODULE, invalid},
                     loop(Acc)
            end;
        { logout, From, Username } ->
            case maps:find(Username, Acc) of
                {ok, {Passwd, _ }} -> From ! {?MODULE, ok},
                                      loop(maps:update(Username, { Passwd, false}, Acc));
                _ -> From ! {?MODULE, invalid},
                     loop(Acc)
            end;
        { online, From } ->
            From ! {?MODULE, [Username || {Username, {_, true}} <- maps:to_list(Acc)]},
            loop(Acc);
        _ -> loop(Acc)
    end.

rpc(Request) ->
    ?MODULE ! Request,
    receive
        {?MODULE, Res } -> Res
    end.

create_account(Username, Password) -> rpc({ create, self(), Username, Password }).
close_account(Username, Password) -> rpc({ close, self(), Username, Password }).
login(Username, Password) -> rpc({ login, self(), Username, Password }).
logout(Username) -> rpc({ logout, self(), Username }).
online() -> rpc({ online, self() }).
