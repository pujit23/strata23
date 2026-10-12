"""
STRATA Python Client SDK.
"""

__version__ = "3.0.0"

class StrataClient:
    """
    STRATA Client connecting to the STRATA REST API Gateway.
    """
    def __init__(self, base_url: str = "http://localhost:8080", api_key: str | None = None):
        self.base_url = base_url.rstrip("/")
        self.api_key = api_key

    def health(self) -> dict:
        return {"status": "ok", "version": __version__}
