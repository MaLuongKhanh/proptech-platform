import pytest
import requests
from io import BytesIO

BASE_URL = "http://localhost:8080/api/sales/contracts"

@pytest.fixture
def auth_token():
    login_url = "http://localhost:8080/api/securities/auth/login"
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(login_url, json=payload)
    assert response.status_code == 200, "Failed to authenticate"
    return response.json()["data"]["accessToken"]

def test_create_contract(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    files = {
        "file": ("test.pdf", BytesIO(b"Dummy PDF content"), "application/pdf"),
        "transactionId": (None, "test-transaction-123")
    }
    response = requests.post(BASE_URL, files=files, headers=headers)
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["id"] is not None

def test_get_contract_by_id(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(f"{BASE_URL}/test-contract-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test contract found")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["id"] == "test-contract-123"

def test_get_all_contracts(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(BASE_URL, headers=headers)
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert isinstance(response.json()["data"], list)

def test_delete_contract(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.delete(f"{BASE_URL}/test-contract-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test contract found")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"