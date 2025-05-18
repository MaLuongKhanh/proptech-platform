import pytest
import requests
import logging

BASE_URL = "http://localhost:8080/api/payments/wallets"

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@pytest.fixture
def auth_token():
    login_url = "http://localhost:8080/api/securities/auth/login"
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(login_url, json=payload)
    assert response.status_code == 200, f"Failed to authenticate: {response.text}"
    return response.json()["data"]["accessToken"]

@pytest.fixture
def test_wallet(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "userId": "test-user-123",
        "balance": 1000.0,
        "currency": "VND"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    if response.status_code != 201:
        pytest.skip(f"Failed to create test wallet: {response.text}")
    return response.json()["data"]["id"]

def test_create_wallet(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "userId": "test-user-456",
        "balance": 2000.0,
        "currency": "VND"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    logger.info(f"Create wallet response: {response.status_code} {response.text}")
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["id"] is not None
    assert response.json()["data"]["userId"] == "test-user-456"

def test_get_wallet_by_user_id(auth_token, test_wallet):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(f"{BASE_URL}/test-user-123", headers=headers)
    logger.info(f"Get wallet response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["userId"] == "test-user-123"

def test_update_wallet(auth_token, test_wallet):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "balance": 1500.0,
        "currency": "VND"
    }
    response = requests.put(f"{BASE_URL}/{test_wallet}", json=payload, headers=headers)
    logger.info(f"Update wallet response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["balance"] == 1500.0

def test_top_up_wallet(auth_token, test_wallet):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.post(f"{BASE_URL}/{test_wallet}/topup?amount=500.0", headers=headers)
    logger.info(f"Top up wallet response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["balance"] == 2000.0  # 1000 + 500

def test_payment_wallet(auth_token, test_wallet):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.post(f"{BASE_URL}/{test_wallet}/payment?amount=200.0", headers=headers)
    logger.info(f"Payment wallet response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["balance"] == 800.0  # 1000 - 200

def test_delete_wallet(auth_token, test_wallet):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.delete(f"{BASE_URL}/{test_wallet}", headers=headers)
    logger.info(f"Delete wallet response: {response.status_code} {response.text}")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"