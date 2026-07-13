"""
A minimal module-level event registry for TinyDB.

Provides :func:`subscribe` to register a callback for an event and
:func:`emit` to notify all subscribers of an event.
"""

from typing import Callable, Dict, List

__all__ = ('subscribe', 'emit')

_subscribers: Dict[str, List[Callable]] = {}


def subscribe(event: str, fn: Callable) -> None:
    """
    Register ``fn`` to be called whenever ``event`` is emitted.
    """
    _subscribers.setdefault(event, []).append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Call every subscriber of ``event``, swallowing subscriber exceptions.
    """
    for fn in _subscribers.get(event, []):
        try:
            fn(**kwargs)
        except Exception:
            pass
