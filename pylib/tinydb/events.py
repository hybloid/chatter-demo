"""
A minimal module-level event registry for TinyDB.

Provides :func:`subscribe` to register callbacks for named events and
:func:`emit` to notify all subscribers of an event. Subscriber exceptions
are swallowed so that emitting an event never breaks the caller.
"""

from typing import Callable, Dict, List

__all__ = ('subscribe', 'emit')

_subscribers: Dict[str, List[Callable]] = {}


def subscribe(event: str, fn: Callable) -> None:
    """
    Register a callback for the given event.

    :param event: the name of the event to subscribe to
    :param fn: the callback to invoke when the event is emitted
    """

    _subscribers.setdefault(event, []).append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Notify all subscribers of the given event.

    Any exception raised by a subscriber is swallowed so emitting an event
    never breaks the caller.

    :param event: the name of the event to emit
    :param kwargs: keyword arguments passed to each subscriber
    """

    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
