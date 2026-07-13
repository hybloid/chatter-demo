"""
A minimal module-level event registry for TinyDB.

Provides :func:`subscribe` to register a callback for a named event and
:func:`emit` to invoke every subscriber of an event. Subscriber exceptions
are swallowed so that emitting an event never breaks the caller.
"""

from collections import defaultdict
from typing import Callable, Dict, List

#: Mapping of event name to the list of subscribed callbacks.
_subscribers: Dict[str, List[Callable]] = defaultdict(list)


def subscribe(event: str, fn: Callable) -> None:
    """
    Register ``fn`` to be called whenever ``event`` is emitted.

    :param event: The event name to subscribe to.
    :param fn: The callback to invoke on emit.
    """

    _subscribers[event].append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Call every subscriber of ``event``, passing along ``kwargs``.

    Exceptions raised by subscribers are swallowed so that emitting an
    event never breaks the caller.

    :param event: The event name to emit.
    """

    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
