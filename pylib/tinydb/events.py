"""
A minimal module-level event registry for TinyDB.

This provides a tiny observer/event hook system so callers can subscribe to
events emitted by TinyDB (e.g. storage reads/writes and table mutations).
"""

from typing import Callable, Dict, List

__all__ = ('subscribe', 'emit')

#: Maps event names to the list of subscribed callbacks.
_subscribers: Dict[str, List[Callable]] = {}


def subscribe(event: str, fn: Callable) -> None:
    """
    Subscribe a callback to an event.

    :param event: the event name to subscribe to
    :param fn: the callback invoked when the event is emitted
    """
    _subscribers.setdefault(event, []).append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Emit an event, calling every subscriber of that event.

    Exceptions raised by subscribers are swallowed so that emitting an event
    never interferes with the operation that triggered it.

    :param event: the event name to emit
    :param kwargs: keyword arguments passed to each subscriber
    """
    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
