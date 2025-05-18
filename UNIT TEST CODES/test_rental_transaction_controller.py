import pytest
import requests

BASE_URL = "http://localhost:8080/api/rentals/transactions"

@pytest.fixture
def auth_token():
    login_url = "http://localhost:8080/api/securities/auth/login"
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(login_url, json=payload)
    assert response.status_code == 200, "Failed to authenticate"
    return response.json()["data"]["accessToken"]

def test_create_rental_transaction(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "amount": 5000000.0,
        "currency": "VND",
        "propertyId": "test-property-123",
        "tenantId": "test-user-123"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["id"] is not None

def test_get_rental_transaction_by_id(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(f"{BASE_URL}/test-transaction-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test transaction found")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["id"] == "test-transaction-123"

def test_get_all_rental_transactions(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(BASE_URL, headers=headers)
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert isinstance(response.json()["data"], list)

def test_delete_rental_transaction(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.delete(f"{BASE_URL}/test-transaction-123", headers=headers)
    if response.status_code == 404:
        pytest.skip("No test transaction found")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"