"""
A minimal module-level event registry for TinyDB.

Provides :func:`subscribe` to register a callback for an event and
:func:`emit` to notify all subscribers of an event. Subscriber exceptions
are swallowed so a misbehaving subscriber cannot break the caller.
"""

from collections import defaultdict
from typing import Callable, Dict, List

_subscribers: Dict[str, List[Callable]] = defaultdict(list)


def subscribe(event: str, fn: Callable) -> None:
    """
    Register ``fn`` as a subscriber for ``event``.
    """
    _subscribers[event].append(fn)


def emit(event: str, **kwargs) -> None:
    """
    Notify every subscriber of ``event``, swallowing subscriber exceptions.
    """
    for fn in _subscribers[event]:
        try:
            fn(**kwargs)
        except Exception:
            pass
