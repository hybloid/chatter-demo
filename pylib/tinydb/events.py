"""
A minimal module-level event registry for TinyDB.

Provides :func:`subscribe` to register a callback for an event and
:func:`emit` to notify all subscribers of an event. Exceptions raised by
subscribers are swallowed so that emitting an event never disrupts the
operation that triggered it.
"""

from typing import Callable, Dict, List

#: Maps an event name to the list of subscribed callbacks.
_subscribers: Dict[str, List[Callable]] = {}


def subscribe(event: str, fn: Callable) -> None:
    """
    Register ``fn`` to be called whenever ``event`` is emitted.

    :param event: The name of the event to subscribe to.
    :param fn: The callback to invoke on emit.
    """
    _subscribers.setdefault(event, []).append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Call every subscriber of ``event`` with the given keyword arguments.

    Exceptions raised by subscribers are swallowed.

    :param event: The name of the event to emit.
    """
    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
