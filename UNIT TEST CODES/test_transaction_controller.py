import pytest
import requests
import json

BASE_URL = "http://localhost:8080/api/payments/transactions"

@pytest.fixture
def auth_token():
    login_url = "http://localhost:8080/api/securities/auth/login"
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(login_url, json=payload)
    assert response.status_code == 200, "Failed to authenticate"
    return response.json()["data"]["accessToken"]

def test_create_payment_transaction(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "amount": 100.0,
        "currency": "VND",
        "description": "Test transaction",
        "walletId": "test-wallet-123"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["id"] is not None

def test_get_payment_transaction_by_id(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    # Assuming a transaction with ID "test-transaction-123" exists
    response = requests.get(f"{BASE_URL}/test-transaction-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test transaction found")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["id"] == "test-transaction-123"

def test_get_all_payment_transactions(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(BASE_URL, headers=headers)
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert isinstance(response.json()["data"], list)

def test_update_payment_transaction(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "amount": 150.0,
        "description": "Updated transaction"
    }
    response = requests.put(f"{BASE_URL}/test-transaction-123", json=payload, headers=headers)
    if response.status_code == 404:
        pytest.skip("No test transaction found")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["amount"] == 150.0

def test_delete_payment_transaction(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.delete(f"{BASE_URL}/test-transaction-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test transaction found")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"