create(RoomList) ->
  receive
    { find, Name, Pid } ->
      case maps:find(Name, RoomList) of
        { ok, Room } -> Pid ! { Room, self() };
        error -> Room = spawn(fun() -> room([]) end),
                 Pid ! { Room, self() },
                 room_manager(maps:put(Name, Room, RoomList))
      end
  end.
