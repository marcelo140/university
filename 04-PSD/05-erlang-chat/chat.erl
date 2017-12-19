-module(chat).
-export([server/1]).

server(Port) ->
  RoomManager = spawn(fun() -> manager(#{}) end),
  register(room_manager, RoomManager),
  room_manager ! { find, "default", self() },
  {ok, LSock} = gen_tcp:listen(Port, [binary, {packet, line}, {reuseaddr, true}]),
  receive
    { Room, RoomManager } -> acceptor(LSock, Room)
  end.

acceptor(LSock, Room) ->
  {ok, Sock} = gen_tcp:accept(LSock),
  spawn(fun() -> acceptor(LSock, Room) end),
  Room ! {enter, self()},
  user(Sock, Room).

manager(RoomList) ->
  receive
    { find, Name, Pid } ->
      case maps:find(Name, RoomList) of
        { ok, Room } -> Pid ! { Room, self() };
        error -> Room = spawn(fun() -> room([]) end),
                 Pid ! { Room, self() },
                 manager(maps:put(Name, Room, RoomList))
      end
  end.

room(Pids) ->
  receive
    {enter, Pid} ->
      io:format("user entered~n", []),
      room([Pid | Pids]);
    {line, Data} = Msg ->
      io:format("received ~p~n", [Data]),
      [Pid ! Msg || Pid <- Pids],
      room(Pids);
    {leave, Pid} ->
      io:format("user left~n", []),
      room(Pids -- [Pid])
  end.

user(Sock, Room) ->
  receive
    {tcp, _, Data} ->
      gen_tcp:send(Sock, Data),
      user(Sock, Room);
    {line, Data} ->
      case Data of
        "\\room " ++ RoomName ->
          New = room_manager ! {find, RoomName, self()},
          Room ! {leave, self()},
          New ! {enter, self()},
          user(Sock, New); 
        _ ->
          Room ! {line, Data},
          user(Sock, Room)
      end;
    {tcp_closed, _} ->
      Room ! {leave, self()};
    {tcp_error, _, _} ->
      Room ! {leave, self()}
  end.

