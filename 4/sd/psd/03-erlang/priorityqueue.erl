-module(priorityqueue).
-export([create/0, enqueue/3, dequeue/1]).

create() -> #{}.

enqueue(PriQueue, Item, Priority) ->
    Queue = maps:get(Priority, PriQueue, myqueue:create()),
    Enqueued = myqueue:enqueue(Queue, Item),
    maps:put(Priority, Enqueued, PriQueue).

dequeue(PriQueue) ->
    case maps:keys(PriQueue) of
        [] -> empty;
        Keys -> HighestKey = lists:min(Keys),
                Queue = maps:get(HighestKey, PriQueue),
                dequeue(PriQueue, Queue, HighestKey)
    end.

dequeue(PriQueue, Queue, Key) ->
    case myqueue:dequeue(Queue) of
        { {[],[]}, Value } -> 
            NewQueue = maps:remove(Key, PriQueue),
            { NewQueue, Value };
        { Dequeued, Value } ->
            NewQueue = maps:put(Key, Dequeued,PriQueue),
            { NewQueue, Value }
    end.
