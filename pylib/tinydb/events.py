"""
A minimal module-level observer/event registry for TinyDB.

Subscribers register callables for named events using :func:`subscribe` and
events are dispatched to every subscriber using :func:`emit`. Exceptions
raised by subscribers are swallowed so that emitting an event never disrupts
the caller.
"""

from typing import Callable, Dict, List

__all__ = ('subscribe', 'emit')

_subscribers: Dict[str, List[Callable]] = {}


def subscribe(event: str, fn: Callable) -> None:
    """
    Register ``fn`` to be called whenever ``event`` is emitted.

    :param event: the name of the event to subscribe to
    :param fn: the callable to invoke when the event is emitted
    """
    _subscribers.setdefault(event, []).append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Call every subscriber of ``event`` with the given keyword arguments.

    Exceptions raised by subscribers are swallowed so that emitting an event
    never disrupts the caller.

    :param event: the name of the event to emit
    """
    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
